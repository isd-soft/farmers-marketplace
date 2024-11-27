import {
  script
} from "./chunk-Y2ESNEQ6.js";
import "./chunk-CUHSAYRT.js";
import "./chunk-RSMNO5O2.js";
import {
  BaseStyle
} from "./chunk-O4J5F3AB.js";
import {
  createElementBlock,
  mergeProps,
  openBlock
} from "./chunk-IJV5NOMV.js";

// node_modules/@primevue/core/baseinput/index.mjs
var script2 = {
  name: "BaseInput",
  "extends": script,
  props: {
    size: {
      type: String,
      "default": null
    },
    fluid: {
      type: Boolean,
      "default": null
    },
    variant: {
      type: String,
      "default": null
    }
  },
  inject: {
    $parentInstance: {
      "default": void 0
    },
    $pcFluid: {
      "default": void 0
    }
  },
  computed: {
    $variant: function $variant() {
      var _this$variant;
      return (_this$variant = this.variant) !== null && _this$variant !== void 0 ? _this$variant : this.$primevue.config.inputStyle || this.$primevue.config.inputVariant;
    },
    $fluid: function $fluid() {
      var _this$fluid;
      return (_this$fluid = this.fluid) !== null && _this$fluid !== void 0 ? _this$fluid : !!this.$pcFluid;
    },
    // @deprecated use $fluid instead
    hasFluid: function hasFluid() {
      return this.$fluid;
    }
  }
};

// node_modules/primevue/inputtext/style/index.mjs
var theme = function theme2(_ref) {
  var dt = _ref.dt;
  return "\n.p-inputtext {\n    font-family: inherit;\n    font-feature-settings: inherit;\n    font-size: 1rem;\n    color: ".concat(dt("inputtext.color"), ";\n    background: ").concat(dt("inputtext.background"), ";\n    padding-block: ").concat(dt("inputtext.padding.y"), ";\n    padding-inline: ").concat(dt("inputtext.padding.x"), ";\n    border: 1px solid ").concat(dt("inputtext.border.color"), ";\n    transition: background ").concat(dt("inputtext.transition.duration"), ", color ").concat(dt("inputtext.transition.duration"), ", border-color ").concat(dt("inputtext.transition.duration"), ", outline-color ").concat(dt("inputtext.transition.duration"), ", box-shadow ").concat(dt("inputtext.transition.duration"), ";\n    appearance: none;\n    border-radius: ").concat(dt("inputtext.border.radius"), ";\n    outline-color: transparent;\n    box-shadow: ").concat(dt("inputtext.shadow"), ";\n}\n\n.p-inputtext:enabled:hover {\n    border-color: ").concat(dt("inputtext.hover.border.color"), ";\n}\n\n.p-inputtext:enabled:focus {\n    border-color: ").concat(dt("inputtext.focus.border.color"), ";\n    box-shadow: ").concat(dt("inputtext.focus.ring.shadow"), ";\n    outline: ").concat(dt("inputtext.focus.ring.width"), " ").concat(dt("inputtext.focus.ring.style"), " ").concat(dt("inputtext.focus.ring.color"), ";\n    outline-offset: ").concat(dt("inputtext.focus.ring.offset"), ";\n}\n\n.p-inputtext.p-invalid {\n    border-color: ").concat(dt("inputtext.invalid.border.color"), ";\n}\n\n.p-inputtext.p-variant-filled {\n    background: ").concat(dt("inputtext.filled.background"), ";\n}\n\n.p-inputtext.p-variant-filled:enabled:hover {\n    background: ").concat(dt("inputtext.filled.hover.background"), ";\n}\n\n.p-inputtext.p-variant-filled:enabled:focus {\n    background: ").concat(dt("inputtext.filled.focus.background"), ";\n}\n\n.p-inputtext:disabled {\n    opacity: 1;\n    background: ").concat(dt("inputtext.disabled.background"), ";\n    color: ").concat(dt("inputtext.disabled.color"), ";\n}\n\n.p-inputtext::placeholder {\n    color: ").concat(dt("inputtext.placeholder.color"), ";\n}\n\n.p-inputtext.p-invalid::placeholder {\n    color: ").concat(dt("inputtext.invalid.placeholder.color"), ";\n}\n\n.p-inputtext-sm {\n    font-size: ").concat(dt("inputtext.sm.font.size"), ";\n    padding-block: ").concat(dt("inputtext.sm.padding.y"), ";\n    padding-inline: ").concat(dt("inputtext.sm.padding.x"), ";\n}\n\n.p-inputtext-lg {\n    font-size: ").concat(dt("inputtext.lg.font.size"), ";\n    padding-block: ").concat(dt("inputtext.lg.padding.y"), ";\n    padding-inline: ").concat(dt("inputtext.lg.padding.x"), ";\n}\n\n.p-inputtext-fluid {\n    width: 100%;\n}\n");
};
var classes = {
  root: function root(_ref2) {
    var instance = _ref2.instance, props = _ref2.props;
    return ["p-inputtext p-component", {
      "p-filled": instance.$filled,
      "p-inputtext-sm p-inputfield-sm": props.size === "small",
      "p-inputtext-lg p-inputfield-lg": props.size === "large",
      "p-invalid": instance.$invalid,
      "p-variant-filled": instance.$variant === "filled",
      "p-inputtext-fluid": instance.$fluid
    }];
  }
};
var InputTextStyle = BaseStyle.extend({
  name: "inputtext",
  theme,
  classes
});

// node_modules/primevue/inputtext/index.mjs
var script$1 = {
  name: "BaseInputText",
  "extends": script2,
  style: InputTextStyle,
  provide: function provide() {
    return {
      $pcInputText: this,
      $parentInstance: this
    };
  }
};
var script3 = {
  name: "InputText",
  "extends": script$1,
  inheritAttrs: false,
  methods: {
    onInput: function onInput(event) {
      this.writeValue(event.target.value, event);
    }
  },
  computed: {
    attrs: function attrs() {
      return mergeProps(this.ptmi("root", {
        context: {
          filled: this.$filled,
          disabled: this.disabled
        }
      }), this.formField);
    }
  }
};
var _hoisted_1 = ["value", "disabled", "aria-invalid"];
function render(_ctx, _cache, $props, $setup, $data, $options) {
  return openBlock(), createElementBlock("input", mergeProps({
    type: "text",
    "class": _ctx.cx("root"),
    value: _ctx.d_value,
    disabled: _ctx.disabled,
    "aria-invalid": _ctx.$invalid || void 0,
    onInput: _cache[0] || (_cache[0] = function() {
      return $options.onInput && $options.onInput.apply($options, arguments);
    })
  }, $options.attrs), null, 16, _hoisted_1);
}
script3.render = render;
export {
  script3 as default
};
//# sourceMappingURL=primevue_inputtext.js.map