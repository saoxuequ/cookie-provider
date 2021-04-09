# cookie-provider

peeking at cookies of chrome on mac osx

# example:
CookieProvider cookieProvider = OsxChromeCookieProviders.newOsxChromeCookieProviderBySystemEnv();
cookieProvider.provide(new CookieCriterion("baidu.com", "/"));