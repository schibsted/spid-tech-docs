/*global cull, bane */

(function () {
  var c = cull;

  var eventHub = bane.createEventEmitter();

  function insertAfter(newElement, referenceElement) {
    referenceElement.parentNode.insertBefore(newElement, referenceElement.nextSibling);
  }

  function setupTabContent(tabs) {
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
        eventHub.on("language:" + node.innerHTML, function () {
          selectTab(node);
        });
      }
    }, tabs.childNodes);

    container.className = "tabs-content";
    selectTab(tabs.childNodes[0]);
    insertAfter(container, tabs);
  }

  function setupTab(tab) {
    tab.onclick = function () {
      eventHub.emit("language:" + tab.innerHTML);
    };
  }

  c.doall(setupTab, document.querySelectorAll(".tab"));
  c.doall(setupTabContent, document.querySelectorAll("div.tabs"));

}());
