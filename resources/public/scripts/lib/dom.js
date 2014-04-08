var dom = (function () {

  function insertAfter(newElement, referenceElement) {
    referenceElement.parentNode.insertBefore(newElement, referenceElement.nextSibling);
  }

  function isAllWS(node) {
    return !(/[^\t\n\r ]/.test(node.data));
  }

  function isIgnorable(node) {
    return (node.nodeType == 8) || // comment
    ((node.nodeType == 3) && isAllWS(node)); // all ws text node
  }

  function nextSibling(element) {
    while ((element = element.nextSibling)) {
      if (!isIgnorable(element)) return element;
    }
    return null;
  }

  function firstChild(element) {
    var res = element.firstChild;
    while (res) {
      if (!isIgnorable(res)) return res;
      res = res.nextSibling;
    }
    return null;
  }

  return {
    insertAfter: insertAfter,
    nextSibling: nextSibling,
    firstChild: firstChild
  };

}());
