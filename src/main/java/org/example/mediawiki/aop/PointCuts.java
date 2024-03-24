package org.example.mediawiki.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {
    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.PagesServiceImpl.get*(..))")
    public void getMethodsPages() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.PagesServiceImpl.create*(..))")
    public void createMethodsPages() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.PagesServiceImpl.update*(..))")
    public void updateMethodsPages() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + ".impl.PagesServiceImpl.delete*(..))")
    public void deleteMethodsPages() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + ".impl.PagesServiceImpl.read*(..))")
    public void readMethodsPages() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.SearchServiceImpl.create*(..))")
    public void createMethodsSearch() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.SearchServiceImpl.delete*(..))")
    public void deleteMethodsSearch() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.SearchServiceImpl.get*(..))")
    public void getMethodsSearch() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.SearchServiceImpl.update(..))")
    public void updateMethodsSearch() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.SearchServiceImpl.read(..))")
    public void readMethodsSearch() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.WordServiceImpl.create*(..))")
    public void createMethodsWord() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.WordServiceImpl.delete*(..))")
    public void deleteMethodsWord() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.WordServiceImpl.update*(..))")
    public void updateMethodsWord() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.WordServiceImpl.get*(..))")
    public void getMethodsWord() {
    }

    @Pointcut("execution(* org.example.mediawiki.service."
            + "impl.WordServiceImpl.read*(..))")
    public void readMethodsWord() {
    }

    @Pointcut("execution(* org.example.mediawiki.controller."
            + "WikiApiRequest.get*(..))")
    public void getMethodsWikiApi() {
    }

    @Pointcut("execution(* org.example.mediawiki.controller."
            + "WikiApiRequest.map*(..))")
    public void mapMethodsWikiApi() {
    }
}
