package com.company.projectManager.common.security.config;

import com.company.projectManager.common.service.RoleService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final RoleService roleService;

    public CustomMethodSecurityExpressionHandler(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
        StandardEvaluationContext context = (StandardEvaluationContext) super.createEvaluationContext(authentication, mi);

        MethodSecurityExpressionOperations delegate = (MethodSecurityExpressionOperations) context.getRootObject().getValue();

        //delegate.getAuthentication() should never be null as the authentication filter is before the authorization one
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(delegate.getAuthentication());

        root.setRoleService(roleService);

        context.setRootObject(root);

        return context;
    }

}
