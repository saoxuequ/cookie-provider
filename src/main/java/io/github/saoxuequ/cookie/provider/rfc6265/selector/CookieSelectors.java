package io.github.saoxuequ.cookie.provider.rfc6265.selector;

import java.util.ArrayList;
import java.util.List;

public class CookieSelectors {
    private CookieSelectors() {}

    public static CookieSelector newSimpleSelector() {
        List<CookieSelector> selectors = new ArrayList<>();
        selectors.add(new DomainSelector());
        selectors.add(new PathSelector());
        selectors.add(new DistinctSelector());
        return new MultiSelector(selectors);
    }
}
