package dev.hero.projectmanager.service;


import dev.hero.projectmanager.exception.AppException;
import dev.hero.projectmanager.model.OrgRole;
import dev.hero.projectmanager.model.Organization;
import dev.hero.projectmanager.model.Plan;
import dev.hero.projectmanager.model.User;
import dev.hero.projectmanager.security.CurrentUserProvider;
import org.springframework.http.HttpStatus;

public abstract class OrgAwareService
{
    protected final CurrentUserProvider currentUserProvider;

    protected OrgAwareService(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    protected User getCurrentUser()
    {
        return currentUserProvider.getCurrentUser();
    }

    protected Organization getCurrentOrg()
    {
        Organization org = getCurrentUser().getOrganization();
        if (org == null) {
            throw new AppException("You are not part of any organization", HttpStatus.FORBIDDEN);
        }

        return org;
    }

    protected void verifyOrgAccess(Organization entityOrg)
    {
        if (!entityOrg.getId().equals(getCurrentOrg().getId())) {
            throw new AppException("Access denied", HttpStatus.FORBIDDEN);
        }
    }

    protected void requireRole(OrgRole... roles)
    {
        if (!hasRole(roles)) {
            throw new AppException("Insufficient permissions", HttpStatus.FORBIDDEN);
        }
    }

    protected boolean hasRole(OrgRole... roles)
    {
        OrgRole userRole = getCurrentUser().getOrgRole();
        for (OrgRole role : roles)
        {
            if (userRole == role)
                return true;
        }

        return false;
    }

    protected boolean isPremium()
    {
        Organization org = getCurrentOrg();
        return org.getPlan() == Plan.PREMIUM || org.getPlan() == Plan.TRIAL;
    }

    protected void requirePremium()
    {
        if (!isPremium()) {
            throw new AppException("This feature requires a Premium plan", HttpStatus.PAYMENT_REQUIRED);
        }
    }
}
