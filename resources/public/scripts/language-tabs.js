/*global cull, bane, dom */

(function () {
  var c = cull;

  var eventHub = bane.createEventEmitter();

  function setLang(lang) {
    lang = lang.toLowerCase() === "clj" ? "clojure" : lang;
    eventHub.emit("language:" + lang.toLowerCase());
    document.cookie = "lang=" + lang.toLowerCase() + "; path=/";
  }

  function setupTabContent(tabs) {
    var container = document.createElement("div");
    var selectedTab;

    function selectTab(tab) {
      if (selectedTab) { selectedTab.className = "tab"; }
      tab.className = "tab active";
      container.innerHTML = dom.nextSibling(tab).innerHTML;
      selectedTab = tab;
    }

    c.doall(function (node) {
      if (node.className === "tab") {
        eventHub.on("language:" + node.innerHTML.toLowerCase(), function () {
          selectTab(node);
        });
      }
    }, tabs.childNodes);

    container.className = "tabs-content";
    selectTab(dom.firstChild(tabs));
    dom.insertAfter(container, tabs);
  }

  function setupTab(tab) {
    tab.onclick = function () {
      setLang(tab.innerHTML);
    };
  }

  c.doall(setupTab, document.querySelectorAll(".tab"));
  c.doall(setupTabContent, document.querySelectorAll("div.tabs"));

  function cookieValue(key) {
    return document.cookie.replace(new RegExp("(?:(?:^|.*;\s*)" + key + "\s*\=\s*([^;]*).*$)|^.*$"), "$1");
  }

  var matches = window.location.href.match(/[&\?]lang=([a-zA-Z]+)/);

  if (matches && matches[1]) {
    setLang(matches[1]);
  } else if (cookieValue("lang")) {
    setLang(cookieValue("lang"));
  }

}());
