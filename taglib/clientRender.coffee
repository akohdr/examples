cl = (s) -> console.log(s)

code = -> (
    # Get handle to tagClient
    tc = exports.tagClient
    # create alias to fullTag renderer
    r = tc.toTagUsing tc.fullTag
    
    # render a tag
    tag = r {b:{_:'hello'}}
    # define jquery path to replacement site
    site = 'div.site1'
        
    # Some debug info
    cl "RENDER: #{tag}"
    # seperate call retains chrome console formatting
    cl 'FOUND:'
    cl $(site)
    
    # do replacement using jquery.html()
    $(site).html(t)
    
    d = {_:'Nested'}
    d = {div:d} for [1..3]
    tag = r d
    
    cl tag
    $(site).html(tag)
)

# execute it
code()

# dump src and ........
cl code


