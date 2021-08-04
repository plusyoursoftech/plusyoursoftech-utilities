package com.alfresco.utilities.saml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.impl.AssertionImpl;
import org.opensaml.saml2.core.impl.AttributeStatementImpl;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.schema.impl.XSStringImpl;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DecodeSAMLResponse {
	private static Log logger = LogFactory.getLog(DecodeSAMLResponse.class);
	private SAMLAssertionUserMapping userMapping = new SAMLAssertionUserMapping();
	public static void main(String[] args) {


		//{"userName":"username","password":"admin","firstName":"firstname","lastName":"lastname","email":"user@gmail.com","disableAccount":false,"quota":-1,"groups":[]}
		
		DecodeSAMLResponse decodeSAMLResponse =new DecodeSAMLResponse();
		SAMLAssertionUserMapping userMapping = decodeSAMLResponse.userMapping;
		StringBuilder body = new StringBuilder("{");

		body.append("\"userName\":\"");
		body.append(userMapping.getId());
		body.append("\",\"password\":\"");
		body.append("password");
		body.append("\",\"firstName\":\"");
		body.append(userMapping.getFirstName());
		body.append("\",\"lastName\":\"");
		body.append(userMapping.getLastName());
		body.append("\",\"email\":\"");
		body.append(userMapping.getEmail());
		body.append("\",\"disableAccount\":false,\"quota\":-1,\"groups\":[]");
		
        body.append('}');
		
        System.out.println(body);
        /*
		
		String req = "PHNhbWxwOlJlc3BvbnNlIFZlcnNpb249IjIuMCIgSUQ9InN5dUtiQU9qNzVEMGR0MlA0TV8ubHBEWUFNYyIgSXNzdWVJbnN0YW50PSIyMDIxLTA3LTMwVDIxOjE2OjIwLjM0NVoiIERlc3RpbmF0aW9uPSJodHRwOi8vYnRsbjAwMjE3Ny5jb3JwLmFkczozNTAxMS9zaGFyZS9wYWdlL3NhbWwtYXV0aG5yZXNwb25zZSIgeG1sbnM6c2FtbHA9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpwcm90b2NvbCI+PHNhbWw6SXNzdWVyIHhtbG5zOnNhbWw9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iPlBpbmdGZWRlcmF0ZVNQPC9zYW1sOklzc3Vlcj48ZHM6U2lnbmF0dXJlIHhtbG5zOmRzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIj4KPGRzOlNpZ25lZEluZm8+CjxkczpDYW5vbmljYWxpemF0aW9uTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8xMC94bWwtZXhjLWMxNG4jIi8+CjxkczpTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNyc2Etc2hhMjU2Ii8+CjxkczpSZWZlcmVuY2UgVVJJPSIjc3l1S2JBT2o3NUQwZHQyUDRNXy5scERZQU1jIj4KPGRzOlRyYW5zZm9ybXM+CjxkczpUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjZW52ZWxvcGVkLXNpZ25hdHVyZSIvPgo8ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8xMC94bWwtZXhjLWMxNG4jIi8+CjwvZHM6VHJhbnNmb3Jtcz4KPGRzOkRpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3NoYTI1NiIvPgo8ZHM6RGlnZXN0VmFsdWU+YWNWTFArZ29OTURyS1JrWkZhUllIVmFFVlBxOUlIVGdCRW5xMU5OUzRhcz08L2RzOkRpZ2VzdFZhbHVlPgo8L2RzOlJlZmVyZW5jZT4KPC9kczpTaWduZWRJbmZvPgo8ZHM6U2lnbmF0dXJlVmFsdWU+CmFqZitHaCs0OGhMTW5aRTVQRm5yeFJEVTNWbEo5THJVN2d1MU94VlZveGRkTEJxaVBCenpnZ2ZCakVwSDFncWtOZncvNnVGbmlmd3oKaXp2a3d4L3dLaFM1aUV2bk5LQWNFNzUvUjJjL0ZsZWJYbEx1ajRzdWFTaVIvcFFybUl3c3RrV3JCU3pYUUROQ1J1YmxUcGVMVnUvWQpNM3ZzallodFRJcEFGejNlVGxuL1RSd3owVFZCMkliN0QyRzFnVzBkSk81TmFCSXNDTWh5Qnk4d0NqdHZkNnd0aWtESmJjb3lzNEFFCi9RV3lLTnMrc01KSXFuQUNuZURSbHFIZEkzc1VUaldjVzVDWE1VVThIMEljY2RBNFFZSkJ0Z3hHQ1N5aFBxYTJsMXV6MUptbWttdHgKVS9rTnhkMm5OcllVRHl0NmJ5YnRSYlFhS3VFMTVvZGJkOE5mUnc9PQo8L2RzOlNpZ25hdHVyZVZhbHVlPgo8L2RzOlNpZ25hdHVyZT48c2FtbHA6U3RhdHVzPjxzYW1scDpTdGF0dXNDb2RlIFZhbHVlPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6c3RhdHVzOlN1Y2Nlc3MiLz48L3NhbWxwOlN0YXR1cz48c2FtbDpBc3NlcnRpb24gSUQ9IlNZU2ZiLVpkdzFjd0p0TTZtaC1HWUlLdll2cSIgSXNzdWVJbnN0YW50PSIyMDIxLTA3LTMwVDIxOjE2OjIxLjAzOFoiIFZlcnNpb249IjIuMCIgeG1sbnM6c2FtbD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI+PHNhbWw6SXNzdWVyPlBpbmdGZWRlcmF0ZVNQPC9zYW1sOklzc3Vlcj48c2FtbDpTdWJqZWN0PjxzYW1sOk5hbWVJRCBGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjEuMTpuYW1laWQtZm9ybWF0OnVuc3BlY2lmaWVkIj4yMTgxOTI1OS0zOWNmLTRmMTMtODQ5OS0yNzU1M2U3YjRmZjg8L3NhbWw6TmFtZUlEPjxzYW1sOlN1YmplY3RDb25maXJtYXRpb24gTWV0aG9kPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6Y206YmVhcmVyIj48c2FtbDpTdWJqZWN0Q29uZmlybWF0aW9uRGF0YSBSZWNpcGllbnQ9Imh0dHA6Ly9idGxuMDAyMTc3LmNvcnAuYWRzOjM1MDExL3NoYXJlL3BhZ2Uvc2FtbC1hdXRobnJlc3BvbnNlIiBOb3RPbk9yQWZ0ZXI9IjIwMjEtMDctMzBUMjE6MjE6MjEuMDM4WiIvPjwvc2FtbDpTdWJqZWN0Q29uZmlybWF0aW9uPjwvc2FtbDpTdWJqZWN0PjxzYW1sOkNvbmRpdGlvbnMgTm90QmVmb3JlPSIyMDIxLTA3LTMwVDIxOjExOjIxLjAzOFoiIE5vdE9uT3JBZnRlcj0iMjAyMS0wNy0zMFQyMToyMToyMS4wMzhaIj48c2FtbDpBdWRpZW5jZVJlc3RyaWN0aW9uPjxzYW1sOkF1ZGllbmNlPkFMRlJFU0NPLURWPC9zYW1sOkF1ZGllbmNlPjwvc2FtbDpBdWRpZW5jZVJlc3RyaWN0aW9uPjwvc2FtbDpDb25kaXRpb25zPjxzYW1sOkF1dGhuU3RhdGVtZW50IFNlc3Npb25JbmRleD0iU1lTZmItWmR3MWN3SnRNNm1oLUdZSUt2WXZxIiBBdXRobkluc3RhbnQ9IjIwMjEtMDctMzBUMjE6MTY6MjAuMzc3WiI+PHNhbWw6QXV0aG5Db250ZXh0PjxzYW1sOkF1dGhuQ29udGV4dENsYXNzUmVmPnVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphYzpjbGFzc2VzOnVuc3BlY2lmaWVkPC9zYW1sOkF1dGhuQ29udGV4dENsYXNzUmVmPjwvc2FtbDpBdXRobkNvbnRleHQ+PC9zYW1sOkF1dGhuU3RhdGVtZW50PjxzYW1sOkF0dHJpYnV0ZVN0YXRlbWVudD48c2FtbDpBdHRyaWJ1dGUgTmFtZT0ic21wUGVybWlzc2lvbkxpc3QiIE5hbWVGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphdHRybmFtZS1mb3JtYXQ6dXJpIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4c2k6dHlwZT0ieHM6c3RyaW5nIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiPlt7cmVzb3VyY2VJZD0yMzQ5NTA2NTEsIHJvbGVJZD0yNjEsIHJvbGVOYW1lPUZGSF9NRU1CRVIsIHJlc291cmNlVHlwZT1DUH0sIHtyZXNvdXJjZUlkPTIzNDk1MDY1MSwgcm9sZUlkPTEwNDI0LCByb2xlTmFtZT1TTVBfVVNFUl9BRE1JTiwgcmVzb3VyY2VUeXBlPUJPfSwge3Jlc291cmNlSWQ9MjM0OTUwNjUxLCByb2xlSWQ9MTYyNTMsIHJvbGVOYW1lPWRvY3JlcG9fcmVhZCwgcmVzb3VyY2VUeXBlPUJPfSwge3Jlc291cmNlSWQ9MjM0OTUwNjUxLCByb2xlSWQ9MTYyNTQsIHJvbGVOYW1lPWRvY3JlcG9fd3JpdGUsIHJlc291cmNlVHlwZT1CT30sIHtyZXNvdXJjZUlkPTIzNDk1MDY1MSwgcm9sZUlkPTE2NDU0LCByb2xlTmFtZT1zb25hciwgcmVzb3VyY2VUeXBlPUJPfSwge3Jlc291cmNlSWQ9MjM0OTUwNjUxLCByb2xlSWQ9MTY0NTUsIHJvbGVOYW1lPWVIZWFsdGggQ0FUU0EsIHJlc291cmNlVHlwZT1CT30sIHtyZXNvdXJjZUlkPTIzNDk1MDY1MSwgcm9sZUlkPTE2NDU2LCByb2xlTmFtZT1Qb3J0YWwgVXNlciBHb0MsIHJlc291cmNlVHlwZT1CT30sIHtyZXNvdXJjZUlkPTIzNDk1MDY1MSwgcm9sZUlkPTE2NDU3LCByb2xlTmFtZT1BZG1pbmlzdHJhdG9yIENBVFNBLCByZXNvdXJjZVR5cGU9Qk99LCB7cmVzb3VyY2VJZD0yMzQ5NTA2NTEsIHJvbGVJZD0xNjQ1OCwgcm9sZU5hbWU9VXNlciBNYW5hZ2VtZW50IENBVFNBLCByZXNvdXJjZVR5cGU9Qk99LCB7cmVzb3VyY2VJZD0yMzQ5NTA2NTEsIHJvbGVJZD0xNjQ1OSwgcm9sZU5hbWU9RG9jdW1lbnQgUmVwb3NpdG9yeSBDQVRTQSwgcmVzb3VyY2VUeXBlPUJPfSwge3Jlc291cmNlSWQ9MjM0OTUwNjUxLCByb2xlSWQ9MTY0NjAsIHJvbGVOYW1lPURPQ1JFUE9fVVNFUiwgcmVzb3VyY2VUeXBlPUJPfSwge3Jlc291cmNlSWQ9MjM0OTUwNjUxLCByb2xlSWQ9MTY0NjEsIHJvbGVOYW1lPVJQVF9TVkNfTUFQLCByZXNvdXJjZVR5cGU9Qk99LCB7cmVzb3VyY2VJZD0yMzQ5NTA2NTEsIHJvbGVJZD0xNjQ2Miwgcm9sZU5hbWU9UlBUX1NWQ19MVkxfRFNIQlJELCByZXNvdXJjZVR5cGU9Qk99LCB7cmVzb3VyY2VJZD0yMzQ5NTA2NTEsIHJvbGVJZD0xNjQ2Mywgcm9sZU5hbWU9UlBUX1NWQ19JTkNJREVOVCwgcmVzb3VyY2VUeXBlPUJPfSwge3Jlc291cmNlSWQ9MjM0OTUwNjUxLCByb2xlSWQ9MTY0NTMsIHJvbGVOYW1lPUNBVFNBIFVzZXIsIHJlc291cmNlVHlwZT1CT30sIHtyZXNvdXJjZUlkPTIzNDk1MDY1MSwgcm9sZUlkPTEwMDEsIHJvbGVOYW1lPVNNUDJfQ1VTVE9NRVJfTUVNQkVSLCByZXNvdXJjZVR5cGU9Qk99XTwvc2FtbDpBdHRyaWJ1dGVWYWx1ZT48L3NhbWw6QXR0cmlidXRlPjxzYW1sOkF0dHJpYnV0ZSBOYW1lPSJmaXJzdE5hbWUiIE5hbWVGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphdHRybmFtZS1mb3JtYXQ6dW5zcGVjaWZpZWQiPjxzYW1sOkF0dHJpYnV0ZVZhbHVlIHhzaTp0eXBlPSJ4czpzdHJpbmciIHhtbG5zOnhzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+c3NvPC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU+PHNhbWw6QXR0cmlidXRlIE5hbWU9Imxhc3ROYW1lIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OnVuc3BlY2lmaWVkIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4c2k6dHlwZT0ieHM6c3RyaW5nIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiPmNhdHNhPC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU+PHNhbWw6QXR0cmlidXRlIE5hbWU9InRlc3RhdHQiIE5hbWVGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphdHRybmFtZS1mb3JtYXQ6dW5zcGVjaWZpZWQiPjxzYW1sOkF0dHJpYnV0ZVZhbHVlIHhzaTp0eXBlPSJ4czpzdHJpbmciIHhtbG5zOnhzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+MjE4MTkyNTktMzljZi00ZjEzLTg0OTktMjc1NTNlN2I0ZmY4PC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU+PHNhbWw6QXR0cmlidXRlIE5hbWU9ImF1dGhlbnRpY2F0aW9uZG9tYWluIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OnVuc3BlY2lmaWVkIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4c2k6dHlwZT0ieHM6c3RyaW5nIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiPmdvYzwvc2FtbDpBdHRyaWJ1dGVWYWx1ZT48L3NhbWw6QXR0cmlidXRlPjxzYW1sOkF0dHJpYnV0ZSBOYW1lPSJ1dWlkIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OnVuc3BlY2lmaWVkIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4c2k6dHlwZT0ieHM6c3RyaW5nIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiPjIxODE5MjU5LTM5Y2YtNGYxMy04NDk5LTI3NTUzZTdiNGZmODwvc2FtbDpBdHRyaWJ1dGVWYWx1ZT48L3NhbWw6QXR0cmlidXRlPjxzYW1sOkF0dHJpYnV0ZSBOYW1lPSJlbWFpbCIgTmFtZUZvcm1hdD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmF0dHJuYW1lLWZvcm1hdDpiYXNpYyI+PHNhbWw6QXR0cmlidXRlVmFsdWUgeHNpOnR5cGU9InhzOnN0cmluZyIgeG1sbnM6eHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hIiB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIj5zc28uY2F0c2FAY2ktcWEuY29tPC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU+PHNhbWw6QXR0cmlidXRlIE5hbWU9InVzZXJuYW1lIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OnVuc3BlY2lmaWVkIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4c2k6dHlwZT0ieHM6c3RyaW5nIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiPnNzby5jYXRzYTwvc2FtbDpBdHRyaWJ1dGVWYWx1ZT48L3NhbWw6QXR0cmlidXRlPjwvc2FtbDpBdHRyaWJ1dGVTdGF0ZW1lbnQ+PC9zYW1sOkFzc2VydGlvbj48L3NhbWxwOlJlc3BvbnNlPg==";
		try {
			 SAMLAssertionUserMapping base64DecodedMessage = decodeSAMLResponse.getBase64DecodedMessage(req);
			logger.info(" userMapping : "+base64DecodedMessage);
		} catch (MessageDecodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/	
	}

	public SAMLAssertionUserMapping getBase64DecodedMessage(String encodedMessage) throws MessageDecodingException {
		logger.info("encodedMessage \n"+encodedMessage);
		if (DatatypeHelper.isEmpty(encodedMessage)) {
			if (DatatypeHelper.isEmpty(encodedMessage)) {
				throw new MessageDecodingException("SAMLMessage didn't contain SAMLResponse or SAMLRequest.");
			}
		}
		
		  if(logger.isTraceEnabled()) logger.trace("Base64 decoding SAML message:" +  encodedMessage);
		 
		byte decodedBytes[] = Base64.decode(encodedMessage);
		if (decodedBytes == null) {
			throw new MessageDecodingException("Couldn't Base64 decode SAML message");
		}
		String decodedResponse = null;
		try {
			decodedResponse = new String(decodedBytes, 0, decodedBytes.length, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		
		logger.info("decodedResponse \n"+decodedResponse);
		parseSamlResponse(decodedResponse);
		return userMapping;
	}
	
	
	private void parseSamlResponse(String responseSaml) {
		
		
		InputStream targetStream = new ByteArrayInputStream(responseSaml.getBytes());
		// Initialize the library
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		// Get parser pool manager
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);
		try {
			Document inCommonMDDoc = ppMgr.parse(targetStream);
			Element metadataRoot = inCommonMDDoc.getDocumentElement();
			// Get apropriate unmarshaller
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);

			// Unmarshall using the document root element, an EntitiesDescriptor in this case
			try {
				// EntitiesDescriptor inCommonMD = (EntitiesDescriptor) unmarshaller.unmarshall(metadataRoot);
				XMLObject unmarshall = unmarshaller.unmarshall(metadataRoot);
				if(unmarshall.hasChildren()) {
					List<XMLObject> orderedChildren = unmarshall.getOrderedChildren();
					orderedChildren.forEach(xmlObject->{
						logger.info(xmlObject);
						if(xmlObject instanceof AssertionImpl  ) {
							AssertionImpl  assertionImpl = (AssertionImpl)xmlObject;
							List<XMLObject> orderedChildren2 = assertionImpl.getOrderedChildren();
							orderedChildren2.forEach(asserObj->{
								logger.info(asserObj);
								if(asserObj instanceof AttributeStatementImpl) {
									logger.info("<=====================AttributeStatement===========================>");
									AttributeStatementImpl  attributeStmtObj = (AttributeStatementImpl)asserObj;
									List<XMLObject> attributeStmtObjs = attributeStmtObj.getOrderedChildren();
									attributeStmtObjs.forEach(attributeStmt->{
									String name = attributeStmt.getDOM().getAttribute("Name");
									logger.info(" name ==>"+name);
									List<XMLObject> attributeValues =attributeStmt.getOrderedChildren();
									attributeValues.forEach(attValueObj->{
										XSStringImpl xsStringImpl = (XSStringImpl)attValueObj;
										logger.info(" value ==>"+xsStringImpl.getValue());
										setAssertionUserMapping(name,xsStringImpl.getValue());
									});
									
								});
								}
								
								
							});
						}
					});
				}
				
			} catch (UnmarshallingException e) {
				logger.info(e);
			}

		} catch (XMLParserException e) {
			logger.info(e);
		}
	
	}
	
	
	    public SAMLAssertionUserMapping setAssertionUserMapping(String assertionName, String value)
		{
			if (UserConstraints.USER_NAME.equalsIgnoreCase(assertionName)) {
				userMapping.setId(value);
			}
			if (UserConstraints.EMAIL.equalsIgnoreCase(assertionName)) {
				userMapping.setEmail(value);
			}
			if (UserConstraints.FRIST_NAME.equalsIgnoreCase(assertionName)) {
				userMapping.setFirstName(value);
			}
			if (UserConstraints.LAST_NAME.equalsIgnoreCase(assertionName)) {
				userMapping.setLastName(value);
			}
			if (UserConstraints.UUID.equalsIgnoreCase(assertionName)) {
				userMapping.setUuid(value);
			}
			if (UserConstraints.PERMISSION.equalsIgnoreCase(assertionName)) {
				userMapping.setPermissions(value);
			}
			if (UserConstraints.AUTH_DOMAIN.equalsIgnoreCase(assertionName)) {
				userMapping.setAuthenticationDomain(value);
			}
			return userMapping;
		}
	    
	    
}
