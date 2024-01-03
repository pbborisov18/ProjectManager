package com.company.projectManager.common.security.config;

import com.company.projectManager.common.service.UsersBusinessUnitsService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component("customAuthorizationManager")
public class CustomAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final UsersBusinessUnitsService usersBusinessUnitsService;

    @Autowired
    public CustomAuthorizationManager(UsersBusinessUnitsService usersBusinessUnitsService) {
        this.usersBusinessUnitsService = usersBusinessUnitsService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation invocation) {

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(invocation.getMethod().getAnnotation(PreAuthorize.class).value());

        CustomMethodSecurityExpressionHandler c = new CustomMethodSecurityExpressionHandler(usersBusinessUnitsService);
        EvaluationContext ec = c.createEvaluationContext(authentication,invocation);
        boolean granted = Boolean.TRUE.equals(expression.getValue(ec, Boolean.class));

        return new AuthorizationDecision(granted);
    }

}
