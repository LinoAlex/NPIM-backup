package com.netphenix.npim.admin.controller;

//import com.netphenix.npim.OAuth.Sheduler;
import com.netphenix.npim.utils.service.DataSourceUtils;
import com.netphenix.npim.OAuth.service.OAuthSelectorImpl;
import com.netphenix.npim.authorization.TokenTemplate;
import com.netphenix.npim.connect.connection.NPIMConnection;
import com.netphenix.npim.utils.service.UrlParser;
import java.util.Map;
//import com.netphenix.npim.OAuth.TokenService;
//import com.netphenix.npim.OAuth.UiService;
//import com.netphenix.npim.OAuth.DataSource;
//import com.netphenix.npim.OAuth.TokenDetails;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.view.RedirectView;
import com.netphenix.npim.utils.service.UrlParser;

/**
 *
 * @author Lino
 */
@RequestMapping(value = "/social")
@Component
@Scope("session")
public class OAuthController {

    @Autowired
    OAuthSelectorImpl oAuthSelector;
    private final UrlParser urlParser = new UrlParser();
    private final DataSourceUtils dataSourceUtils = new DataSourceUtils();
//    @Autowired
//    Sheduler Sheduler;
//    @Autowired
//    TokenService tokenService;
//    @Autowired
//    UiService uiService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthController.class);
    String successUrl;
    public static MultiValueMap<String, Object> oAuthData;
    private static NPIMConnection connection;

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView signin(HttpServletRequest request,
            HttpServletResponse response, Map<String, Object> queryValues) {
        String domainName = request.getParameter("domainName");
        String port = request.getParameter("port");
        System.out.println("domain name ----------->" + domainName);
        successUrl = "http://" + domainName + "/NPIM/admin/social/success";
        String authorizeUrl = "http://" + domainName + "/NPIM/admin/social/error";
        LOGGER.debug("in sign in");
        try {
            LOGGER.debug("sessionId signin" + request.getSession().getId());
            if (queryValues.isEmpty()) {
                LOGGER.debug("request params--->"+request.getQueryString());
            queryValues = urlParser.queryToMap(request.getQueryString());
            LOGGER.debug("dataSource------------->" + queryValues.get("apiSource"));
            }
            String sourceUrl = dataSourceUtils.generateSourceUrl(queryValues);
            if (sourceUrl.equals("missingParams")) {
                LOGGER.debug("in missing params--------->");
            } else if (sourceUrl.equals("authenticate")) {
                LOGGER.debug("in authenticate --------------->");
                oAuthData = oAuthSelector.generateOAuthUrl(request);
                authorizeUrl = (String) oAuthData.getFirst("authorizeUrl");
            } else {
                LOGGER.debug("in default ----------->" + sourceUrl);
                connection = NPIMConnection.getInstance(sourceUrl);
                LOGGER.debug("connection----->" + connection);
            }
        } catch (Exception ex) {
            LOGGER.error("insufficient params", ex);
        }
        RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
                true);

        return new ModelAndView(redirectView);
    }

    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public Map getData(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> queryValues = urlParser.queryToMap(request.getQueryString());
            String dataSetUrl = dataSourceUtils.generateDataSeturl(queryValues);
            LOGGER.debug("dataSetUrl-------->" + dataSetUrl);
            LOGGER.debug("connection----->" + connection);
            LOGGER.debug("dataSet data-------->" + connection.getData(dataSetUrl));
            return connection.getData(dataSetUrl);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/callback", params = {"code"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView tokenGenerator(@RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        LOGGER.debug("session id in callback-------->" + session.getId());
        try {
            LOGGER.debug("code------------->" + code);
            LOGGER.debug("oAuthData------------->" + oAuthData);
            oAuthData.add("code", code);
            TokenTemplate tokenTemplate = oAuthSelector.getTokenDetails(oAuthData);
            insertIntoDb(oAuthData, tokenTemplate, request);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);
            Map<String, Object> clientData = dataSourceUtils.mapValues(tokenTemplate, oAuthData);
            signin(request, response, clientData);
            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/callback", params = {"code", "state"}, method = RequestMethod.GET)
    public ModelAndView tokenGenerator(@RequestParam("code") String code, @RequestParam("state") String state,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        LOGGER.debug("session id in callback-------->" + session.getId());
        try {
            oAuthData.add("code", code);
            TokenTemplate tokenTemplate = oAuthSelector.getTokenDetails(oAuthData);
            insertIntoDb(oAuthData, tokenTemplate, request);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);
            Map<String, Object> clientData = dataSourceUtils.mapValues(tokenTemplate, oAuthData);
            signin(request, response, clientData);
            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/callback", params = {"oauth_token", "oauth_verifier"}, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView oAuth1TokenGenerator(@RequestParam("oauth_token") String oauth_token, @RequestParam("oauth_verifier") String oauth_verifier,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        LOGGER.debug("session id in callback-------->" + session.getId());
        try {
            oAuthData.add("oauth_token", oauth_token);
            oAuthData.add("oauth_verifier", oauth_verifier);
            TokenTemplate tokenTemplate = oAuthSelector.getTokenDetails(oAuthData);
            insertIntoDb(oAuthData, tokenTemplate, request);
            RedirectView redirectView = new RedirectView(successUrl, true, true,
                    true);
            Map<String, Object> clientData = dataSourceUtils.mapValues(tokenTemplate, oAuthData);
            signin(request, response, clientData);
            return new ModelAndView(redirectView);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    @RequestMapping(value = "/saveToken", method = RequestMethod.POST)
//    @ResponseBody
//    public TokenDetails saveToken(@RequestBody TokenDetails tokenDetails) {
//        insertTokenDetails(tokenDetails);
//        return tokenDetails;
//    }
//
//    @RequestMapping(value = "/oauthStatus", method = RequestMethod.PUT)
//    @ResponseBody
//    public DataSource updateOauthStatus(@RequestBody DataSource dataSource) {
//        return uiService.update(dataSource);
//    }
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    @ResponseBody
    public String errorNotifier() {
        return "Error Occured Check if Entered details are correct";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    @ResponseBody
    public String successNotifier() {
        return "The tokens are generated successfully";
    }

    private void insertIntoDb(MultiValueMap<String, Object> oAuthData, TokenTemplate tokenTemplate, HttpServletRequest request) {
//        tokenService.insertIntoDb(oAuthData, tokenTemplate, request);
    }

//    private void insertTokenDetails(TokenDetails tokenDetails) {
//        tokenService.insertTokenDetails(tokenDetails);
//    }
}
