package com.task.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AemPracticeNavigationItems {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String pathToPage;

    public String getTitle() {
        return title;
    }

    public String getPathToPage() {
        return pathToPage;
    }
}