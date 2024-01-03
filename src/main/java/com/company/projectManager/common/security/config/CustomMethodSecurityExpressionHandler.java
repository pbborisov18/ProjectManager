package com.company.projectManager.common.security.config;

import com.company.projectManager.common.service.UsersBusinessUnitsService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final UsersBusinessUnitsService usersBusinessUnitsService;

    public CustomMethodSecurityExpressionHandler(UsersBusinessUnitsService usersBusinessUnitsService) {
        this.usersBusinessUnitsService = usersBusinessUnitsService;
    }

    @Override
    public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
        StandardEvaluationContext context = (StandardEvaluationContext) super.createEvaluationContext(authentication, mi);

        MethodSecurityExpressionOperations delegate = (MethodSecurityExpressionOperations) context.getRootObject().getValue();

        //delegate.getAuthentication() should never be null as the authentication filter is before the authorization one
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(delegate.getAuthentication());

        root.setUsersBusinessUnitsService(usersBusinessUnitsService);

        context.setRootObject(root);

        return context;
    }

}
