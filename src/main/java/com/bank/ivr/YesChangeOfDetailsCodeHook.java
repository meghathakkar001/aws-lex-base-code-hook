package com.bank.ivr;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.runtime.events.LexEvent;
import com.bank.ivr.model.LexResponse;

public interface YesChangeOfDetailsCodeHook {
	@LambdaFunction(functionName="YesChangeOfDetailsCodeHook")
    public LexResponse invokeHook(LexEvent request);
}
