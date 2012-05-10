// Generated by CoffeeScript 1.3.1
(function() {

  exports.tagClient = {
    isObject: function(o) {
      return 'object' === typeof o;
    },
    isArray: function(o) {
      return {}.toString.call(o).indexOf('Array') >= 0;
    },
    attrKeys: function(o, x) {
      var k, _results;
      _results = [];
      for (k in o) {
        if (!this.isObject(o[k]) && !((x != null ? x.indexOf(k) : void 0) >= 0)) {
          _results.push(k);
        }
      }
      return _results;
    },
    attrs: function(o, x) {
      var k, _i, _len, _ref, _results;
      _ref = this.attrKeys(o, x);
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        k = _ref[_i];
        _results.push("" + (k.replace('_', ':')) + "='" + o[k] + "'");
      }
      return _results;
    },
    objKeys: function(o, x) {
      var k, _results;
      _results = [];
      for (k in o) {
        if (this.isObject(o[k]) && !((x != null ? x.indexOf(k) : void 0) >= 0)) {
          _results.push(k);
        }
      }
      return _results;
    },
    objs: function(o, x) {
      var k, _i, _len, _ref, _results;
      _ref = this.objKeys(o, x);
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        k = _ref[_i];
        _results.push(o[k]);
      }
      return _results;
    },
    unaryTag: function(n) {
      var _this = this;
      return function(o) {
        return "<" + n + " " + (_this.attrs(o).join(' ')) + "/>";
      };
    },
    openTag: function(n) {
      var _this = this;
      return function(o, x) {
        return "<" + n + " " + (_this.attrs(o, x).join(' ')) + ">";
      };
    },
    closeTag: function(n) {
      var _this = this;
      return function() {
        return "</" + n + ">";
      };
    },
    emptyTag: function(n) {
      var _this = this;
      return function(o) {
        return [_this.openTag(n)(o), _this.closeTag(n)].join("");
      };
    },
    fullTag: function(n) {
      var _this = this;
      return function(o) {
        var body, close, open, _inner;
        _inner = function(n, o) {
          var body, i, k, ks, name, _results;
          ks = [].concat(_this.objKeys(o));
          name = _this.isArray(o) ? function(k) {
            return n;
          } : function(k) {
            return k;
          };
          _results = [];
          for (i in ks) {
            _results.push(body = (k = ks[i], (_this.fullTag(name(k)))(o[k])));
          }
          return _results;
        };
        open = _this.openTag(n)(o, ["_"]);
        body = o["_"] || "";
        body += _inner(n, o).join('\n');
        close = (_this.closeTag(n))();
        if (_this.isArray(o)) {
          return body;
        } else {
          return open + body + close;
        }
      };
    },
    firstProp: function(o) {
      return Object.getOwnPropertyNames(o)[0];
    },
    toTagUsing: function(f) {
      var _this = this;
      return function(o) {
        var n;
        n = _this.firstProp(o);
        return f.call(_this, n)(o[n]);
      };
    }
  };

}).call(this);