package net.defmsy.awsserverlesspoc;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

public class AwsServerlessPocStack extends Stack {
    public AwsServerlessPocStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsServerlessPocStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        new WidgetService(this, "Widgets");
    }
}
