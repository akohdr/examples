# Copyright (c) 2012 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.

# Crafted to minimize dependencies allowing code to be shared in client context
# native impls are avoided to allow codebase to be injected into client through websocket

exports.tagClient = {

isObject: (o) -> 'object' == typeof o
isArray: (o) -> {}.toString.call(o).indexOf('Array') >= 0

# all attribute values are quoted and _ replaced with : for xmlns tags
# both attrKeys and objKeys accept optional list of elements to exclude
attrKeys: (o,x) -> k for k of o when !@isObject(o[k]) && !(x?.indexOf(k)>=0)
attrs: (o,x) -> "#{k.replace('_',':')}='#{o[k]}'" for k in @attrKeys(o,x)
objKeys: (o,x) -> k for k of o when @isObject(o[k]) && !(x?.indexOf(k)>=0)
objs: (o,x) -> o[k] for k in @objKeys(o,x)

# tag renderers TODO memoize on tagname with wrapping func?
unaryTag: (n) -> (o) => "<#{n} #{@attrs(o).join(' ')}/>"
openTag: (n) -> (o,x) => "<#{n} #{@attrs(o,x).join(' ')}>"
closeTag: (n) -> () => "</#{n}>"
emptyTag: (n) -> (o) => [@openTag(n)(o), @closeTag n].join("")
fullTag: (n) -> (o) =>
    _inner = (n,o) =>
        ks = [].concat (@objKeys o)
        name = if @isArray(o) then (k) -> n else (k) -> k
        body = ((k = ks[i]; (@fullTag name(k)) o[k])) for i of ks
    open = (@openTag(n) o,["_"]) # brackets are killing me
    body = o["_"] || "" # start with text between tags if any
    body += _inner(n,o).join('\n')  # go recursive in new function scope
    close = (@closeTag n)()
    if @isArray(o) then body else open + body + close

firstProp: (o) -> Object.getOwnPropertyNames(o)[0]
toTagUsing: (f) -> (o) => n = @firstProp(o); f.call(@,n)(o[n])

}

