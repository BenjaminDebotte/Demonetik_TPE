package emerikbedouin.demonetiktpe;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by emerikbedouin on 08/02/16.
 */
public class HttpRequestParameters {
    private String url;
    private String requestType;
    private String dataReturnType;
    private HashMap<String, String> parameters;
    private String result;

    public HttpRequestParameters(String url, String requestType, String dataReturnType, HashMap<String, String> parameters) {
        this.url = url;
        this.requestType = requestType;
        this.dataReturnType = dataReturnType;
        this.parameters = parameters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDataReturnType() {
        return dataReturnType;
    }

    public void setDataReturnType(String dataReturnType) {
        this.dataReturnType = dataReturnType;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
