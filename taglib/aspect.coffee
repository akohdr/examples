# Copyright (c) 2012 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.
#
# Following module provides aspect style debug wrapping functions
log = if log then log else (-> i=0; (s) -> console.log "#{i++}: #{s}")()

# proxies are function objects with delegate property
newProxy = (o) -> (P = ->).prototype.delegate = o; new P

defDelegateProp = (o) ->
    (n) ->
        Object.defineProperty o,n,
            enumerable: true
            get: -> o.delegate[n]
            set: (v) -> o.delegate[n] = v

before = (b,f,a,ic,n) -> ->
    b.apply(ic,arguments)
    f().apply(ic,arguments)

after  = (b,f,a,ic,n) -> ->
    r = f().apply(ic,arguments)
    a.call(ic,r)
    r

around = (b,f,a,ic,n) -> ->
    b.apply(ic,arguments)
    r = f().apply(ic,arguments)
    a.call(ic,r)
    r

debug = (adviceFn) ->
    (n,ic) ->
        adviceFn(
            -> log "DEBUG #{n}(#{JSON.stringify arguments})"
            -> ic.delegate[n] # we must derefernce delegate on each call in case it has changed
            -> log "DEBUG #{n}() = #{JSON.stringify arguments}"
            ic
            n
        )

wrapObjUsing = (wrapFn) ->
    (o) ->
        p = newProxy o
        d = p.delegate
        defProp = defDelegateProp p
        (
            e = d[n]
            p[n] =
                if typeof e == 'function'
                then wrapFn(n,p)
                else v = d[n]; defProp n; p[n] = v
        ) for n of d
        p

snapOff = (f1,f2,ic) ->
    (args...) ->
        if args[0] == '#snap'
        then f1
        else f2.apply(ic,arguments)

snapFn = (adviceFn) ->
    (b,f,a,ic,n) ->
        f = -> ic
        snapOff(f(), adviceFn(b,f,a,ic,n), ic)

pureFuncDebugWrapper = debug snapFn around

snapMethod = (adviceFn) ->
    (b,f,a,ic,n) ->
        f = -> ic[n]
        snapOff(f(), adviceFn(b,f,a,ic,n), ic)

methodDebugWrapper = debug snapMethod around

snapDelegate = (adviceFn) ->
    (b,f,a,ic,n) ->
        snapOff((-> f().apply(ic,arguments)), adviceFn(b,f,a,ic,n), ic)

objectDebugWrapper = (o) ->
    wrapObjUsing( debug snapDelegate around )(o)

proxyWrapper = wrapObjUsing( (n,ic) -> -> (ic.delegate[n]).apply(ic,arguments) )

# exports acts as public interface so we provide meaningful client facing names
exports.aspect = {
    debugFn: (func, name='func') -> pureFuncDebugWrapper(name, func)
    debugMethod: (object, name) -> methodDebugWrapper(name, object)
    debugObject: (object) -> objectDebugWrapper(object)

    proxy: (object) -> proxyWrapper(object)

}


