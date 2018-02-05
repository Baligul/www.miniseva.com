package com.miniseva.common.handlebars;

import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import com.github.jknack.handlebars.Options;

import java.io.IOException;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.miniseva.app.state.State;

@HandlebarsHelper
public class CustomHelper {
    CharSequence foo() {
        return "bar";
    }

    public CharSequence ifeq(Long value, Options options) throws IOException {
        if (value == null || options.param(0) == null) return options.inverse();
            
        if (value.equals(options.param(0))) {
            return options.fn();
        }
        
        return options.inverse();
    }

    public CharSequence ifeqInt(Integer value, Options options) throws IOException {
        if (value.equals(options.param(0))) {
            return options.fn();
        }
        
        return options.inverse();
    }

    public CharSequence ifeqStr(String value, Options options) throws IOException {
        if (value == null || options.param(0) == null) return options.inverse();
        
        if (value.equals(options.param(0))) {
            return options.fn();
        }
        
        return options.inverse();
    }

    public CharSequence ifInclude(List<Long> list, Options options) throws IOException {
        if (list == null || options.param(0) == null) return options.inverse();
        
        if(list.contains((Long) options.param(0))) {
            return options.fn();
        }
        
        return options.inverse();
    }

    public CharSequence ifIncludeInt(List<Integer> list, Options options) throws IOException {
        if (list == null || options.param(0) == null) return options.inverse();
        
        if(list.contains((Integer) options.param(0))) {
            return options.fn();
        }
        
        return options.inverse();
    }

    public CharSequence ifeqState(State value, Options options) throws IOException {
        if (value == null || options.param(0) == null) return options.inverse();
            
        if (value.toString().equals(options.param(0))) {
            return options.fn();
        }
        
        return options.inverse();
    }

    public CharSequence ifneqInt(Integer value, Options options) throws IOException {
        if (!value.equals(options.param(0))) {
            return options.fn();
        }
            
        return options.inverse();
    }

    public CharSequence ifneqStr(String value, Options options) throws IOException {
        if (value == null || options.param(0) == null) return options.fn();
        
        if (!value.equals(options.param(0))) {
            return options.fn();
        }

        return options.inverse();
    }

    public CharSequence inc(Integer value, Options options) throws IOException {
            return String.valueOf(value + 1);
    }

    public CharSequence ifCSVStrInclude(String csvStr, Options options) throws IOException {
        if (csvStr == null || options.param(0) == null) return options.inverse();
        
        List<Long> arrAnswers = Arrays.stream(csvStr.split(",")).map(Long::parseLong).collect(Collectors.toList());
        if(arrAnswers.contains((Long) options.param(0))) {
            return options.fn();
        }

        return options.inverse();
    }

    public CharSequence getDiscount(Integer val, Options options) throws IOException {
        if (val == null || options.param(0) == null) return "";

        int discount = val - (Integer) options.param(0);

        return "" + discount + "";
    }
}