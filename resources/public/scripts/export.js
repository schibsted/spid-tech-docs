document.body.onclick = function (e) {
  if (e.target.tagName === "INPUT" || e.target.tagName === "TEXTAREA") {
    e.target.select();
  }
};