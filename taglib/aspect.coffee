# Copyright (c) 2012 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.
#
# Following module provides aspect style debug wrapping functions
# makes use of coffeescript '...' splats for handling args

snapOff = (cutFn) ->
    (g,f) ->
        (args...) ->
            if args[0] == '@snap'
            then f
            else cutFn(g,f)(args...)

cut = (g,f) ->
    (args...) ->
        g(args...)
        f(args...)

chain = (g,f) ->
    (args...) ->
        f(g(args...))

dumpArgs = (pre) ->
    (args...) ->
        log "#{pre}(#{a for a in args})"

addDebugUsing = (wrappingFn) ->
    (f,n="func") ->
        wrappingFn( (dumpArgs("DEBUG = #{n}")),f )

wrapObjUsing = (wrapper) ->
    (o) ->
        r = {_unwrap: o}
        (
            e = o[n]
            r[n] = if typeof e == 'function'
            then wrapper(e,n)
            else e
        ) for n of o
        r

debugFn = addDebugUsing snapOff cut

debugObj = (o) ->
    wrapObjUsing(debugFn)(o)

exports.aspect = {
    debugFn: debugFn
    debugObj: debugObj

    wrapObjUsing: wrapObjUsing
}


