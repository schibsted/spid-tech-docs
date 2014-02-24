/*global cull */

(function () {
  var c = cull;

  function insertAfter(newElement, referenceElement) {
    referenceElement.parentNode.insertBefore(newElement, referenceElement.nextSibling);
  }

  function setupTabs(tabs) {
    var container = document.createElement("div");
    var selectedTab;

    function selectTab(tab) {
      if (selectedTab) { selectedTab.className = "tab"; }
      tab.className = "tab active";
      container.innerHTML = tab.nextSibling.innerHTML;
      selectedTab = tab;
    }

    c.doall(function (node) {
      if (node.className === "tab") {
        node.onclick = selectTab.bind(null, node);
      }
    }, tabs.childNodes);

    container.className = "tabs-content";
    selectTab(tabs.childNodes[0]);
    insertAfter(container, tabs);
  }

  c.doall(setupTabs, document.querySelectorAll("div.tabs"));

}());
