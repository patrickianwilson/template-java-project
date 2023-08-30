package com.sample.cardinal;


import com.inquestdevops.%%{{ServiceModuleName.lowerCase}}%%.service.ApiException;
import com.inquestdevops.%%{{ServiceModuleName.lowerCase}}%%.service.api.DefaultApi;
import com.inquestdevops.%%{{ServiceModuleName.lowerCase}}%%.service.config.ClientFactory;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by pwilson on 11/2/17.
 */
public class StatusTests {
    static ClientFactory.Environments testEnvironment =
            System.getenv("BUILD_NUMBER") == null ?
                    ClientFactory.Environments.LOCAL :
                    ClientFactory.Environments.DEV;

    static com.inquestdevops.rabbit.client.config.ClientFactory.Environments rabbitTestEnvironment =
            System.getenv("BUILD_NUMBER") == null ?
                    com.inquestdevops.rabbit.client.config.ClientFactory.Environments.LOCAL :
                    com.inquestdevops.rabbit.client.config.ClientFactory.Environments.DEV;

    static DefaultApi cardsharpApi = ClientFactory.getClient(testEnvironment, rabbitTestEnvironment.getBaseUrl(),
            "not-used", "not-used");

    @Test
    public void verifyShallowStatus() throws ApiException {
        cardsharpApi.amIUp();
    }

    @Test
    public void verifyDeepStatus() throws ApiException {
        cardsharpApi.amIReady();
    }
}
