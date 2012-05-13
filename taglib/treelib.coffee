# Copyright (c) 2012 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.

varnames = # for debug/reference
    p: 'parent'
    o: 'object'
    c: 'child'
    e: 'element'
    r: 'result'
    s: 'source'
    d: 'dest'
    v: 'verb'
    w: 'verb2'
    a: 'attacher'
    m: 'detacher'
    x: 'eXclude'
 
exports.treeClient = -> {
    isObject: (o) -> 'object' == typeof o
    isArray: (o) -> {}.toString.call(o).indexOf('Array') >= 0
    attrKeys: (o,x) -> k for k of o when !@isObject(o[k]) && !(x?.indexOf(k)>=0)
    objKeys: (o,x) -> k for k of o when @isObject(o[k]) && !(x?.indexOf(k)>=0)

    props: (o) -> e for e of o
    propsIn: (o) -> e for e in o

    attach: (p) -> (c,o) -> p[c] = o
    detach: (p) -> (c) -> oc = p[c]; delete p[c]; oc

    move: (s,d) ->
        a = @attach d; m = @detach s
        (c) -> a(c,(m(c)))
    copy: (s,d) ->
        a = @attach d
        (c) -> a(c, s[c])
        
    scopy: (s) -> (ps) => # fat arrow
        r = {}; v = @copy(s, r)
        v(c) for c in ps; r
        
    sclone: (s) ->
        (@scopy s)(@attrKeys(s))
    
    dcopy: (s) -> (as,os) =>
        r = @scopy(s)(as)
        a = @attach(r)
        a(c, @dclone(s[c])) for c in os
        r

    dclone: (s) ->
        (@dcopy s)(@attrKeys(s), @objKeys(s))
}


