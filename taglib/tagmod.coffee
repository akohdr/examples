#{tagClient} = require 'taglib.js'
{tagClient} = require 'taglib.coffee'

# PUBLIC API collection of tag functions
exports.tag = {

    clientFuncs:  -> fn+'='+tagClient[fn] for fn of tagClient
    clientCode: -> @clientFuncs().join(';\n')
    sendCtx:  (sender) -> (sender {js: 'tagClient.'+fn+' = '+tagClient[fn]}) for fn of tagClient

    # Commonly used HTML tags
    img: (o) -> unary('img')(o)
    a: (o) -> full('a')(o)
} #tag

exports.imgs = {
    google: 'http://www.google.com/images/srpr/logo3w.png'
    nodejs: 'http://nodejs.org/images/logos/monitor.png'
}

exports.vids = {
    youtube_good_parts: '<iframe width="420" height="315" src="http://www.youtube.com/embed/hQVTIJBZook" frameborder="0" allowfullscreen></iframe>'
    min_good_parts: '<iframe src="http://www.youtube.com/embed/hQVTIJBZook"/>'
    good_parts_js: -> embeddedVid 'http://www.youtube.com/embed/hQVTIJBZook'
}
#tagClient.vid = exports.vids.good_parts_js #TODO remove

exports.tagClient = tagClient

# Aliases used in REPL
global.tag = exports.tag


