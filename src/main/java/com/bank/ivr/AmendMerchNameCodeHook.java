package com.bank.ivr;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.runtime.events.LexEvent;
import com.bank.ivr.model.LexResponse;

public interface AmendMerchNameCodeHook {
	@LambdaFunction(functionName="AmendMerchNameCodeHook")
    public LexResponse invokeHook(LexEvent request);
}
