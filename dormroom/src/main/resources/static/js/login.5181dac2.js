(function(e){function t(t){for(var o,s,i=t[0],l=t[1],u=t[2],f=0,d=[];f<i.length;f++)s=i[f],n[s]&&d.push(n[s][0]),n[s]=0;for(o in l)Object.prototype.hasOwnProperty.call(l,o)&&(e[o]=l[o]);c&&c(t);while(d.length)d.shift()();return a.push.apply(a,u||[]),r()}function r(){for(var e,t=0;t<a.length;t++){for(var r=a[t],o=!0,i=1;i<r.length;i++){var l=r[i];0!==n[l]&&(o=!1)}o&&(a.splice(t--,1),e=s(s.s=r[0]))}return e}var o={},n={login:0},a=[];function s(t){if(o[t])return o[t].exports;var r=o[t]={i:t,l:!1,exports:{}};return e[t].call(r.exports,r,r.exports,s),r.l=!0,r.exports}s.m=e,s.c=o,s.d=function(e,t,r){s.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:r})},s.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},s.t=function(e,t){if(1&t&&(e=s(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var r=Object.create(null);if(s.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var o in e)s.d(r,o,function(t){return e[t]}.bind(null,o));return r},s.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return s.d(t,"a",t),t},s.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},s.p="";var i=window["webpackJsonp"]=window["webpackJsonp"]||[],l=i.push.bind(i);i.push=t,i=i.slice();for(var u=0;u<i.length;u++)t(i[u]);var c=l;a.push([1,"chunk-vendors"]),r()})({1:function(e,t,r){e.exports=r("9eec")},"249c":function(e,t,r){"use strict";var o=r("c227"),n=r.n(o);n.a},4070:function(e,t,r){},4430:function(e,t,r){"use strict";var o,n,a="http://localhost:8080/api/",s="./login.html",i="./index.html",l={login_location:s,index_location:i,host:a},u=l,c=r("2877"),f=Object(c["a"])(u,o,n,!1,null,null,null);t["a"]=f.exports},"5b4e":function(e,t,r){"use strict";var o=r("9d01"),n=r.n(o);n.a},"653a":function(e,t,r){"use strict";var o=r("4070"),n=r.n(o);n.a},"9d01":function(e,t,r){},"9eec":function(e,t,r){"use strict";r.r(t);r("cadf"),r("551c"),r("f751"),r("097d");var o=r("2b0e"),n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"container",attrs:{id:"box"}},[r("div",{attrs:{id:"main"}},[r("router-view")],1)])},a=[],s={},i=s,l=(r("5b4e"),r("653a"),r("2877")),u=Object(l["a"])(i,n,a,!1,null,"7dcb0a63",null),c=u.exports,f=r("8c4f"),d=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{attrs:{id:"login"}},[r("div",{staticClass:"title"},[e._v("用户登录 | LOGIN")]),r("hr"),r("el-form",{ref:"loginForm",attrs:{model:e.loginForm,rules:e.rules,"label-width":"50px","label-position":"left"}},[r("el-form-item",{attrs:{label:"账号",prop:"account"}},[r("el-input",{model:{value:e.loginForm.account,callback:function(t){e.$set(e.loginForm,"account",t)},expression:"loginForm.account"}})],1),r("el-form-item",{attrs:{label:"密码",prop:"password"}},[r("el-input",{attrs:{type:"password"},model:{value:e.loginForm.password,callback:function(t){e.$set(e.loginForm,"password",t)},expression:"loginForm.password"}})],1),r("el-checkbox",{staticClass:"rememberPwd",model:{value:e.rememberPwd,callback:function(t){e.rememberPwd=t},expression:"rememberPwd"}},[e._v("记住密码")]),r("el-form-item",{staticClass:"btn_box"},[r("el-button",{attrs:{type:"primary"},on:{click:e.loginClick}},[e._v("登 录")]),r("el-button",{attrs:{type:"success"},on:{click:function(t){return e.$router.push("/register")}}},[e._v("注 册")])],1)],1)],1)},p=[],m=(r("28a5"),{data:function(){return{loginForm:{account:"",password:""},rules:{account:[{required:!0,message:"请输入账号",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]},rememberPwd:!0}},methods:{loginClick:function(){var e=this;this.$refs.loginForm.validate(function(t){if(!t)return!1;e.axios.post("/login",e.loginForm).then(function(t){1==t.data.code?(e.$message("登录成功，正在跳转"),console.log(t.data),e.rememberPwd?(e.setCookie("account",e.loginForm.account),e.setCookie("password",e.loginForm.password)):(e.setCookie("account",""),e.setCookie("password","")),localStorage.setItem("token",t.data.data.token),setTimeout(function(){window.location.href=e.COMMON.index_location},1e3)):e.$message("账号或密码错误")}).catch(function(){e.$message("账号不存在")})})},setCookie:function(e,t,r){var o=new Date;o.setTime(o.getTime()+24*r*60*60*1e3);var n="expires="+o.toGMTString();document.cookie=e+"="+t+"; "+n},getCookie:function(e){for(var t=e+"=",r=document.cookie.split(";"),o=0;o<r.length;o++){var n=r[o].trim();if(0==n.indexOf(t))return n.substring(t.length,n.length)}return""}},mounted:function(){this.loginForm.account=this.getCookie("account"),this.loginForm.password=this.getCookie("password")}}),g=m,b=(r("249c"),Object(l["a"])(g,d,p,!1,null,"0d755fa3",null)),v=b.exports,h=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{attrs:{id:"register"}},[r("div",{staticClass:"title"},[e._v("用户注册 | REGISTER")]),r("hr"),r("el-form",{ref:"registerForm",attrs:{model:e.registerForm,rules:e.rules,"label-width":"80px","label-position":"left"}},[r("el-form-item",{attrs:{label:"账号",prop:"account"}},[r("el-input",{model:{value:e.registerForm.account,callback:function(t){e.$set(e.registerForm,"account",t)},expression:"registerForm.account"}})],1),r("el-form-item",{attrs:{label:"用户名",prop:"name"}},[r("el-input",{model:{value:e.registerForm.name,callback:function(t){e.$set(e.registerForm,"name",t)},expression:"registerForm.name"}})],1),r("el-form-item",{attrs:{label:"密码",prop:"password"}},[r("el-input",{attrs:{type:"password"},model:{value:e.registerForm.password,callback:function(t){e.$set(e.registerForm,"password",t)},expression:"registerForm.password"}})],1),r("el-form-item",{attrs:{label:"重复密码",prop:"password2",required:""}},[r("el-input",{attrs:{type:"password"},model:{value:e.registerForm.password2,callback:function(t){e.$set(e.registerForm,"password2",t)},expression:"registerForm.password2"}})],1),r("el-form-item",{staticClass:"btn_box"},[r("el-button",{attrs:{type:"primary"},on:{click:e.registerClick}},[e._v("注 册")]),r("el-button",{attrs:{type:"info"},on:{click:function(t){return e.$router.push("/login")}}},[e._v("返回登录")])],1)],1)],1)},w=[],F=(r("7f7f"),{data:function(){var e=this,t=function(t,r,o){""===r?o(new Error("请再次输入密码")):r!==e.registerForm.password?o(new Error("两次输入密码不一致!")):o()};return{registerForm:{account:"",name:"",password:"",password2:""},rules:{account:[{required:!0,message:"请输入账号",trigger:"blur"}],name:[{required:!0,message:"请输入用户名",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}],password2:[{validator:t,trigger:["blur","change"]}]}}},methods:{registerClick:function(){var e=this;this.$refs.registerForm.validate(function(t){if(!t)return!1;var r={account:e.registerForm.account,password:e.registerForm.password,name:e.registerForm.name};console.log(r),e.axios.post("/register",r).then(function(t){1==t.data.code?(e.$message("注册成功，请登录"),e.$router.push("/login")):e.$message("该用户名已被占用，注册失败")}).catch(function(){e.$message("无法连接服务器")})})}}}),k=F,x=(r("cedf"),Object(l["a"])(k,h,w,!1,null,"1ca7f62e",null)),y=x.exports;o["default"].use(f["a"]);var _=new f["a"]({routes:[{path:"/",redirect:"/login"},{path:"/login",name:"login",component:v},{path:"/register",name:"register",component:y}]}),$=_,C=r("bc3a"),O=r.n(C),j=r("a7fe"),P=r.n(j),M=r("4430"),S=(r("560b"),r("450d"),r("dcdc")),T=r.n(S),E=(r("b5d8"),r("f494")),q=r.n(E),G=(r("f4f9"),r("c2cc")),I=r.n(G),N=(r("7a0f"),r("0f6c")),R=r.n(N),J=(r("6611"),r("e772")),L=r.n(J),D=(r("1f1a"),r("4e4b")),U=r.n(D),z=(r("425f"),r("4105")),A=r.n(z),B=(r("eca7"),r("3787")),H=r.n(B),K=(r("10cb"),r("f3ad")),Q=r.n(K),V=(r("1951"),r("eedf")),W=r.n(V),X=(r("0fb7"),r("f529")),Y=r.n(X);r("0fae");Y.a.install=function(e){e.prototype.$message=Y.a},o["default"].use(W.a),o["default"].use(Q.a),o["default"].use(H.a),o["default"].use(A.a),o["default"].use(U.a),o["default"].use(L.a),o["default"].use(R.a),o["default"].use(I.a),o["default"].use(Y.a),o["default"].use(q.a),o["default"].use(T.a),o["default"].prototype.COMMON=M["a"],o["default"].use(P.a,O.a),O.a.defaults.baseURL=M["a"].host,new o["default"]({router:$,render:function(e){return e(c)}}).$mount("#app"),o["default"].config.productionTip=!1},c227:function(e,t,r){},cedf:function(e,t,r){"use strict";var o=r("fc9c"),n=r.n(o);n.a},fc9c:function(e,t,r){}});