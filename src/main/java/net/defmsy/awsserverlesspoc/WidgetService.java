package net.defmsy.awsserverlesspoc;

import software.amazon.awscdk.services.apigateway.Resource;
import software.constructs.Construct;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.Bucket;

public class WidgetService extends Construct {

  @SuppressWarnings("serial")
  public WidgetService(Construct scope, String id) {
    super(scope, id);

    Bucket bucket = new Bucket(this, "WidgetStore");

    Function handler = Function.Builder.create(this, "WidgetHandler")
        .runtime(Runtime.NODEJS_14_X)
        .code(Code.fromAsset("resources"))
        .handler("widgets.main")
        .environment(java.util.Map.of(
                "BUCKET", bucket.getBucketName()))
            .build();

    bucket.grantReadWrite(handler);

    RestApi api = RestApi.Builder.create(this, "Widgets-API")
        .restApiName("Widget Service").description("This service services widgets.")
        .build();

    LambdaIntegration getWidgetsIntegration = LambdaIntegration.Builder.create(handler)
        .requestTemplates(java.util.Map.of(
            "application/json", "{ \"statusCode\": \"200\" }"))
        .build();

    api.getRoot().addMethod("GET", getWidgetsIntegration);

    Resource widget = api.getRoot().addResource("{id}");

    LambdaIntegration widgetIntegration = new LambdaIntegration(handler);

    widget.addMethod("POST", widgetIntegration);
    widget.addMethod("GET", widgetIntegration);
    widget.addMethod("DELETE", widgetIntegration);
  }
}
