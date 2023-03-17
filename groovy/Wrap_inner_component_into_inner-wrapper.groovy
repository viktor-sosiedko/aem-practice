import org.apache.sling.settings.SlingSettingsService;

def runModes = getService(SlingSettingsService.class).getRunModes();
if (!runModes.contains('author')) {
    println('Instance is not author. Script will not be executed.');
    return;
}

def QUERY = '''SELECT * FROM [nt:base] WHERE ISDESCENDANTNODE([/content]) 
                and [sling:resourceType]='my-project/components/inner']'''

def queryManager = session.workspace.queryManager;
def result = queryManager.createQuery(QUERY, 'JCR-SQL2').execute();

def replicatedPages = [];
def notReplicatedPages = [];

def dryRun = true;

result.nodes.each {
    node ->
        def parentNode = node.getParent();
        if (!parentNode.hasProperty('sling:resourceType')
                || !'my-project/components/wrapper'.equals(parentNode.getProperty('sling:resourceType').getString())) {
            def wrapper = parentNode.addNode('inner-wrapper');
            wrapper.setProperty('sling:resourceType', 'my-project/components/wrapper');
            parentNode.orderBefore('inner-wrapper', node.getName());
            session.move(node.getPath(), wrapper.getPath() + '/' + node.getName());
            if (!dryRun) {
                session.save();
            }
            processReplication(node, replicatedPages, notReplicatedPages, dryRun);
        }
}

def getPageNode(node) {
    def parentNode = node.getParent();
    if ('cq:Page'.equals(parentNode.getProperty('jcr:primaryType').getString())) {
        return parentNode;
    }
    return getPageNode(parentNode)
}

def processReplication(node, notReplicatedPages, replicatedPages, dryRun) {
    def pageNode = getPageNode(node);
    def pageContent = pageNode.getNode('jcr:content');

    if (pageContent.hasProperty('cq:lastReplicationAction')
            && 'Activate'.equals(pageContent.getProperty('cq:lastReplicationAction').getString())) {

        def lastModified = pageContent.getProperty('cq:lastModified').getDate();
        def lastPublished = pageContent.getProperty('cq:lastReplicated').getDate();

        if (lastModified.after(lastPublished)) {
            notReplicatedPages.add(pageNode.getPath());
        } else {
            replicatedPages.add(pageNode.getPath());
            if (!dryRun) {
                activate(pageNode.getPath());
            }
        }
    } else {
        notReplicatedPages.add(pageNode.getPath());
    }

}

if (dryRun) {
    println("=== Pages to Update and Rplicate ===");
} else {
    println("=== Updated and Rplicated pages ===");
}
replicatedPages.each {
    path -> println(path);
}

if (dryRun) {
    println("=== Pages to Update and NOT Rplicate ===");
} else {
    println("=== Updated and NOT Rplicated pages ===");
}
notReplicatedPages.each {
    path -> println(path);
}