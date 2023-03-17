package com.task.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AemPracticeModel {

    @ValueMapValue
    private String imagePath;

    @ValueMapValue
    private String title;

    @Default(booleanValues = false)
    @ValueMapValue
    private boolean isBackgroundImage;

    @ChildResource
    private List<AemPracticeNavigationItems> navigationItems;

    public String getImagePath() {
        return imagePath;
    }

    public boolean isBackgroundImage() {
        return isBackgroundImage;
    }

    public String getTitle() {
        return title;
    }

    public List<AemPracticeNavigationItems> getNavigationItems() {
        return navigationItems;
    }

}
