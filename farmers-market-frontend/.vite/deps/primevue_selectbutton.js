import {
  Ripple
} from "./chunk-7Z65L2QW.js";
import "./chunk-JVACOY4S.js";
import {
  script
} from "./chunk-Y2ESNEQ6.js";
import "./chunk-CUHSAYRT.js";
import "./chunk-RSMNO5O2.js";
import {
  BaseStyle,
  equals,
  isNotEmpty,
  resolveFieldData
} from "./chunk-O4J5F3AB.js";
import {
  Fragment,
  createBaseVNode,
  createBlock,
  createCommentVNode,
  createElementBlock,
  createSlots,
  mergeProps,
  normalizeClass,
  openBlock,
  renderList,
  renderSlot,
  resolveComponent,
  resolveDirective,
  toDisplayString,
  withCtx,
  withDirectives
} from "./chunk-IJV5NOMV.js";

// node_modules/primevue/togglebutton/style/index.mjs
var theme = function theme2(_ref) {
  var dt = _ref.dt;
  return "\n.p-togglebutton {\n    display: inline-flex;\n    cursor: pointer;\n    user-select: none;\n    align-items: center;\n    justify-content: center;\n    overflow: hidden;\n    position: relative;\n    color: ".concat(dt("togglebutton.color"), ";\n    background: ").concat(dt("togglebutton.background"), ";\n    border: 1px solid ").concat(dt("togglebutton.border.color"), ";\n    padding: ").concat(dt("togglebutton.padding"), ";\n    font-size: 1rem;\n    font-family: inherit;\n    font-feature-settings: inherit;\n    transition: background ").concat(dt("togglebutton.transition.duration"), ", color ").concat(dt("togglebutton.transition.duration"), ", border-color ").concat(dt("togglebutton.transition.duration"), ",\n        outline-color ").concat(dt("togglebutton.transition.duration"), ", box-shadow ").concat(dt("togglebutton.transition.duration"), ";\n    border-radius: ").concat(dt("togglebutton.border.radius"), ";\n    outline-color: transparent;\n    font-weight: ").concat(dt("togglebutton.font.weight"), ";\n}\n\n.p-togglebutton-content {\n    position: relative;\n    display: inline-flex;\n    align-items: center;\n    justify-content: center;\n    gap: ").concat(dt("togglebutton.gap"), ';\n}\n\n.p-togglebutton-label,\n.p-togglebutton-icon {\n    position: relative;\n    transition: none;\n}\n\n.p-togglebutton::before {\n    content: "";\n    background: transparent;\n    transition: background ').concat(dt("togglebutton.transition.duration"), ", color ").concat(dt("togglebutton.transition.duration"), ", border-color ").concat(dt("togglebutton.transition.duration"), ",\n            outline-color ").concat(dt("togglebutton.transition.duration"), ", box-shadow ").concat(dt("togglebutton.transition.duration"), ";\n    position: absolute;\n    inset-inline-start: ").concat(dt("togglebutton.content.left"), ";\n    inset-block-start: ").concat(dt("togglebutton.content.top"), ";\n    width: calc(100% - calc(2 * ").concat(dt("togglebutton.content.left"), "));\n    height: calc(100% - calc(2 * ").concat(dt("togglebutton.content.top"), "));\n    border-radius: ").concat(dt("togglebutton.border.radius"), ";\n}\n\n.p-togglebutton.p-togglebutton-checked::before {\n    background: ").concat(dt("togglebutton.content.checked.background"), ";\n    box-shadow: ").concat(dt("togglebutton.content.checked.shadow"), ";\n}\n\n.p-togglebutton:not(:disabled):not(.p-togglebutton-checked):hover {\n    background: ").concat(dt("togglebutton.hover.background"), ";\n    color: ").concat(dt("togglebutton.hover.color"), ";\n}\n\n.p-togglebutton.p-togglebutton-checked {\n    background: ").concat(dt("togglebutton.checked.background"), ";\n    border-color: ").concat(dt("togglebutton.checked.border.color"), ";\n    color: ").concat(dt("togglebutton.checked.color"), ";\n}\n\n.p-togglebutton:focus-visible {\n    box-shadow: ").concat(dt("togglebutton.focus.ring.shadow"), ";\n    outline: ").concat(dt("togglebutton.focus.ring.width"), " ").concat(dt("togglebutton.focus.ring.style"), " ").concat(dt("togglebutton.focus.ring.color"), ";\n    outline-offset: ").concat(dt("togglebutton.focus.ring.offset"), ";\n}\n\n.p-togglebutton.p-invalid {\n    border-color: ").concat(dt("togglebutton.invalid.border.color"), ";\n}\n\n.p-togglebutton:disabled {\n    opacity: 1;\n    cursor: default;\n    background: ").concat(dt("togglebutton.disabled.background"), ";\n    border-color: ").concat(dt("togglebutton.disabled.border.color"), ";\n    color: ").concat(dt("togglebutton.disabled.color"), ";\n}\n\n.p-togglebutton-icon {\n    color: ").concat(dt("togglebutton.icon.color"), ";\n}\n\n.p-togglebutton:not(:disabled):not(.p-togglebutton-checked):hover .p-togglebutton-icon {\n    color: ").concat(dt("togglebutton.icon.hover.color"), ";\n}\n\n.p-togglebutton.p-togglebutton-checked .p-togglebutton-icon {\n    color: ").concat(dt("togglebutton.icon.checked.color"), ";\n}\n\n.p-togglebutton:disabled .p-togglebutton-icon {\n    color: ").concat(dt("togglebutton.icon.disabled.color"), ";\n}\n\n.p-togglebutton-sm {\n    padding: ").concat(dt("togglebutton.sm.padding"), ";\n    font-size: ").concat(dt("togglebutton.sm.font.size"), ";\n}\n\n.p-togglebutton-lg {\n    padding: ").concat(dt("togglebutton.lg.padding"), ";\n    font-size: ").concat(dt("togglebutton.lg.font.size"), ";\n}\n");
};
var classes = {
  root: function root(_ref2) {
    var instance = _ref2.instance, props = _ref2.props;
    return ["p-togglebutton p-component", {
      "p-togglebutton-checked": instance.active,
      "p-invalid": instance.$invalid,
      "p-togglebutton-sm p-inputfield-sm": props.size === "small",
      "p-togglebutton-lg p-inputfield-lg": props.size === "large"
    }];
  },
  content: "p-togglebutton-content",
  icon: "p-togglebutton-icon",
  label: "p-togglebutton-label"
};
var ToggleButtonStyle = BaseStyle.extend({
  name: "togglebutton",
  theme,
  classes
});

// node_modules/primevue/togglebutton/index.mjs
var script$1 = {
  name: "BaseToggleButton",
  "extends": script,
  props: {
    onIcon: String,
    offIcon: String,
    onLabel: {
      type: String,
      "default": "Yes"
    },
    offLabel: {
      type: String,
      "default": "No"
    },
    iconPos: {
      type: String,
      "default": "left"
    },
    readonly: {
      type: Boolean,
      "default": false
    },
    tabindex: {
      type: Number,
      "default": null
    },
    ariaLabelledby: {
      type: String,
      "default": null
    },
    ariaLabel: {
      type: String,
      "default": null
    },
    size: {
      type: String,
      "default": null
    }
  },
  style: ToggleButtonStyle,
  provide: function provide() {
    return {
      $pcToggleButton: this,
      $parentInstance: this
    };
  }
};
var script2 = {
  name: "ToggleButton",
  "extends": script$1,
  inheritAttrs: false,
  emits: ["change"],
  methods: {
    getPTOptions: function getPTOptions(key) {
      var _ptm = key === "root" ? this.ptmi : this.ptm;
      return _ptm(key, {
        context: {
          active: this.active,
          disabled: this.disabled
        }
      });
    },
    onChange: function onChange(event) {
      if (!this.disabled && !this.readonly) {
        this.writeValue(!this.d_value, event);
        this.$emit("change", event);
      }
    },
    onBlur: function onBlur(event) {
      var _this$formField$onBlu, _this$formField;
      (_this$formField$onBlu = (_this$formField = this.formField).onBlur) === null || _this$formField$onBlu === void 0 || _this$formField$onBlu.call(_this$formField, event);
    }
  },
  computed: {
    active: function active() {
      return this.d_value === true;
    },
    hasLabel: function hasLabel() {
      return isNotEmpty(this.onLabel) && isNotEmpty(this.offLabel);
    },
    label: function label() {
      return this.hasLabel ? this.d_value ? this.onLabel : this.offLabel : " ";
    }
  },
  directives: {
    ripple: Ripple
  }
};
var _hoisted_1 = ["tabindex", "disabled", "aria-pressed", "aria-labelledby", "data-p-checked", "data-p-disabled"];
function render(_ctx, _cache, $props, $setup, $data, $options) {
  var _directive_ripple = resolveDirective("ripple");
  return withDirectives((openBlock(), createElementBlock("button", mergeProps({
    type: "button",
    "class": _ctx.cx("root"),
    tabindex: _ctx.tabindex,
    disabled: _ctx.disabled,
    "aria-pressed": _ctx.d_value,
    onClick: _cache[0] || (_cache[0] = function() {
      return $options.onChange && $options.onChange.apply($options, arguments);
    }),
    onBlur: _cache[1] || (_cache[1] = function() {
      return $options.onBlur && $options.onBlur.apply($options, arguments);
    })
  }, $options.getPTOptions("root"), {
    "aria-labelledby": _ctx.ariaLabelledby,
    "data-p-checked": $options.active,
    "data-p-disabled": _ctx.disabled
  }), [createBaseVNode("span", mergeProps({
    "class": _ctx.cx("content")
  }, $options.getPTOptions("content")), [renderSlot(_ctx.$slots, "default", {}, function() {
    return [renderSlot(_ctx.$slots, "icon", {
      value: _ctx.d_value,
      "class": normalizeClass(_ctx.cx("icon"))
    }, function() {
      return [_ctx.onIcon || _ctx.offIcon ? (openBlock(), createElementBlock("span", mergeProps({
        key: 0,
        "class": [_ctx.cx("icon"), _ctx.d_value ? _ctx.onIcon : _ctx.offIcon]
      }, $options.getPTOptions("icon")), null, 16)) : createCommentVNode("", true)];
    }), createBaseVNode("span", mergeProps({
      "class": _ctx.cx("label")
    }, $options.getPTOptions("label")), toDisplayString($options.label), 17)];
  })], 16)], 16, _hoisted_1)), [[_directive_ripple]]);
}
script2.render = render;

// node_modules/primevue/selectbutton/style/index.mjs
var theme3 = function theme4(_ref) {
  var dt = _ref.dt;
  return "\n.p-selectbutton {\n    display: inline-flex;\n    user-select: none;\n    vertical-align: bottom;\n    outline-color: transparent;\n    border-radius: ".concat(dt("selectbutton.border.radius"), ";\n}\n\n.p-selectbutton .p-togglebutton {\n    border-radius: 0;\n    border-width: 1px 1px 1px 0;\n}\n\n.p-selectbutton .p-togglebutton:focus-visible {\n    position: relative;\n    z-index: 1;\n}\n\n.p-selectbutton .p-togglebutton:first-child {\n    border-inline-start-width: 1px;\n    border-start-start-radius: ").concat(dt("selectbutton.border.radius"), ";\n    border-end-start-radius: ").concat(dt("selectbutton.border.radius"), ";\n}\n\n.p-selectbutton .p-togglebutton:last-child {\n    border-start-end-radius: ").concat(dt("selectbutton.border.radius"), ";\n    border-end-end-radius: ").concat(dt("selectbutton.border.radius"), ";\n}\n\n.p-selectbutton.p-invalid {\n    outline: 1px solid ").concat(dt("selectbutton.invalid.border.color"), ";\n    outline-offset: 0;\n}\n");
};
var classes2 = {
  root: function root2(_ref2) {
    var instance = _ref2.instance;
    return ["p-selectbutton p-component", {
      "p-invalid": instance.$invalid
      // @todo: check
    }];
  }
};
var SelectButtonStyle = BaseStyle.extend({
  name: "selectbutton",
  theme: theme3,
  classes: classes2
});

// node_modules/primevue/selectbutton/index.mjs
var script$12 = {
  name: "BaseSelectButton",
  "extends": script,
  props: {
    options: Array,
    optionLabel: null,
    optionValue: null,
    optionDisabled: null,
    multiple: Boolean,
    allowEmpty: {
      type: Boolean,
      "default": true
    },
    dataKey: null,
    ariaLabelledby: {
      type: String,
      "default": null
    },
    size: {
      type: String,
      "default": null
    }
  },
  style: SelectButtonStyle,
  provide: function provide2() {
    return {
      $pcSelectButton: this,
      $parentInstance: this
    };
  }
};
function _createForOfIteratorHelper(r, e) {
  var t = "undefined" != typeof Symbol && r[Symbol.iterator] || r["@@iterator"];
  if (!t) {
    if (Array.isArray(r) || (t = _unsupportedIterableToArray(r)) || e) {
      t && (r = t);
      var _n = 0, F = function F2() {
      };
      return { s: F, n: function n() {
        return _n >= r.length ? { done: true } : { done: false, value: r[_n++] };
      }, e: function e2(r2) {
        throw r2;
      }, f: F };
    }
    throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.");
  }
  var o, a = true, u = false;
  return { s: function s() {
    t = t.call(r);
  }, n: function n() {
    var r2 = t.next();
    return a = r2.done, r2;
  }, e: function e2(r2) {
    u = true, o = r2;
  }, f: function f() {
    try {
      a || null == t["return"] || t["return"]();
    } finally {
      if (u) throw o;
    }
  } };
}
function _toConsumableArray(r) {
  return _arrayWithoutHoles(r) || _iterableToArray(r) || _unsupportedIterableToArray(r) || _nonIterableSpread();
}
function _nonIterableSpread() {
  throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.");
}
function _unsupportedIterableToArray(r, a) {
  if (r) {
    if ("string" == typeof r) return _arrayLikeToArray(r, a);
    var t = {}.toString.call(r).slice(8, -1);
    return "Object" === t && r.constructor && (t = r.constructor.name), "Map" === t || "Set" === t ? Array.from(r) : "Arguments" === t || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(t) ? _arrayLikeToArray(r, a) : void 0;
  }
}
function _iterableToArray(r) {
  if ("undefined" != typeof Symbol && null != r[Symbol.iterator] || null != r["@@iterator"]) return Array.from(r);
}
function _arrayWithoutHoles(r) {
  if (Array.isArray(r)) return _arrayLikeToArray(r);
}
function _arrayLikeToArray(r, a) {
  (null == a || a > r.length) && (a = r.length);
  for (var e = 0, n = Array(a); e < a; e++) n[e] = r[e];
  return n;
}
var script3 = {
  name: "SelectButton",
  "extends": script$12,
  inheritAttrs: false,
  emits: ["change"],
  methods: {
    getOptionLabel: function getOptionLabel(option) {
      return this.optionLabel ? resolveFieldData(option, this.optionLabel) : option;
    },
    getOptionValue: function getOptionValue(option) {
      return this.optionValue ? resolveFieldData(option, this.optionValue) : option;
    },
    getOptionRenderKey: function getOptionRenderKey(option) {
      return this.dataKey ? resolveFieldData(option, this.dataKey) : this.getOptionLabel(option);
    },
    isOptionDisabled: function isOptionDisabled(option) {
      return this.optionDisabled ? resolveFieldData(option, this.optionDisabled) : false;
    },
    onOptionSelect: function onOptionSelect(event, option, index) {
      var _this = this;
      if (this.disabled || this.isOptionDisabled(option)) {
        return;
      }
      var selected = this.isSelected(option);
      if (selected && !this.allowEmpty) {
        return;
      }
      var optionValue = this.getOptionValue(option);
      var newValue;
      if (this.multiple) {
        if (selected) newValue = this.d_value.filter(function(val) {
          return !equals(val, optionValue, _this.equalityKey);
        });
        else newValue = this.d_value ? [].concat(_toConsumableArray(this.d_value), [optionValue]) : [optionValue];
      } else {
        newValue = selected ? null : optionValue;
      }
      this.writeValue(newValue, event);
      this.$emit("change", {
        event,
        value: newValue
      });
    },
    isSelected: function isSelected(option) {
      var selected = false;
      var optionValue = this.getOptionValue(option);
      if (this.multiple) {
        if (this.d_value) {
          var _iterator = _createForOfIteratorHelper(this.d_value), _step;
          try {
            for (_iterator.s(); !(_step = _iterator.n()).done; ) {
              var val = _step.value;
              if (equals(val, optionValue, this.equalityKey)) {
                selected = true;
                break;
              }
            }
          } catch (err) {
            _iterator.e(err);
          } finally {
            _iterator.f();
          }
        }
      } else {
        selected = equals(this.d_value, optionValue, this.equalityKey);
      }
      return selected;
    }
  },
  computed: {
    equalityKey: function equalityKey() {
      return this.optionValue ? null : this.dataKey;
    }
  },
  directives: {
    ripple: Ripple
  },
  components: {
    ToggleButton: script2
  }
};
var _hoisted_12 = ["aria-labelledby"];
function render2(_ctx, _cache, $props, $setup, $data, $options) {
  var _component_ToggleButton = resolveComponent("ToggleButton");
  return openBlock(), createElementBlock("div", mergeProps({
    "class": _ctx.cx("root"),
    role: "group",
    "aria-labelledby": _ctx.ariaLabelledby
  }, _ctx.ptmi("root")), [(openBlock(true), createElementBlock(Fragment, null, renderList(_ctx.options, function(option, index) {
    return openBlock(), createBlock(_component_ToggleButton, {
      key: $options.getOptionRenderKey(option),
      modelValue: $options.isSelected(option),
      onLabel: $options.getOptionLabel(option),
      offLabel: $options.getOptionLabel(option),
      disabled: _ctx.disabled || $options.isOptionDisabled(option),
      unstyled: _ctx.unstyled,
      size: _ctx.size,
      onChange: function onChange2($event) {
        return $options.onOptionSelect($event, option, index);
      },
      pt: _ctx.ptm("pcToggleButton")
    }, createSlots({
      _: 2
    }, [_ctx.$slots.option ? {
      name: "default",
      fn: withCtx(function() {
        return [renderSlot(_ctx.$slots, "option", {
          option,
          index
        }, function() {
          return [createBaseVNode("span", mergeProps({
            ref_for: true
          }, _ctx.ptm("pcToggleButton")["label"]), toDisplayString($options.getOptionLabel(option)), 17)];
        })];
      }),
      key: "0"
    } : void 0]), 1032, ["modelValue", "onLabel", "offLabel", "disabled", "unstyled", "size", "onChange", "pt"]);
  }), 128))], 16, _hoisted_12);
}
script3.render = render2;
export {
  script3 as default
};
//# sourceMappingURL=primevue_selectbutton.js.map
