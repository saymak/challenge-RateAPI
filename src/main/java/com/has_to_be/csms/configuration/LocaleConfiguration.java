package com.has_to_be.csms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

@Configuration
public class LocaleConfiguration {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        final Locale german = new Locale("de");
        localeResolver.setSupportedLocales(Arrays.asList(Locale.US, german));
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

}
