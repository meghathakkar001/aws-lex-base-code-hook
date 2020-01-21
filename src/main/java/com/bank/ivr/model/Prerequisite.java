package com.bank.ivr.model;

import java.util.Objects;

public class Prerequisite {
    private String intentName;
    private String lambdaCodeHookAlias;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prerequisite that = (Prerequisite) o;
        return Objects.equals(intentName, that.intentName) &&
                Objects.equals(lambdaCodeHookAlias, that.lambdaCodeHookAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intentName, lambdaCodeHookAlias);
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public String getLambdaCodeHookAlias() {
        return lambdaCodeHookAlias;
    }

    public void setLambdaCodeHookAlias(String lambdaCodeHookAlias) {
        this.lambdaCodeHookAlias = lambdaCodeHookAlias;
    }
}
