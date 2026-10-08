const state = { token: localStorage.getItem("aisysToken") || "" };

function headers(json = true) {
  const h = {};
  if (json) h["Content-Type"] = "application/json";
  if (state.token) h.Authorization = "Bearer " + state.token;
  return h;
}

async function api(path, options = {}) {
  const res = await fetch(path, options);
  const text = await res.text();
  try { return { ok: res.ok, status: res.status, body: JSON.parse(text) }; }
  catch { return { ok: res.ok, status: res.status, body: text }; }
}

function show(id, data) {
  document.getElementById(id).textContent = typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

function setSession() {
  document.getElementById("session").textContent = state.token ? "Signed in" : "Not signed in";
}

document.querySelectorAll("nav [data-tab]").forEach(btn => {
  btn.addEventListener("click", () => {
    document.querySelectorAll(".tab").forEach(el => el.classList.add("hidden"));
    document.getElementById(btn.dataset.tab).classList.remove("hidden");
  });
});

document.getElementById("loginBtn").onclick = async () => {
  const result = await api("/api/v1/auth/login", {
    method: "POST",
    headers: headers(),
    body: JSON.stringify({
      username: document.getElementById("username").value,
      password: document.getElementById("password").value
    })
  });
  if (result.body && result.body.token) {
    state.token = result.body.token;
    localStorage.setItem("aisysToken", state.token);
    setSession();
  }
  show("searchOut", result.body);
};

document.getElementById("searchBtn").onclick = async () => {
  const q = encodeURIComponent(document.getElementById("searchQuery").value);
  show("searchOut", (await api("/api/v1/search?query=" + q)).body);
};

document.getElementById("checkoutBtn").onclick = async () => {
  show("circOut", (await api("/api/v1/circulation/checkout", {
    method: "POST", headers: headers(),
    body: JSON.stringify({
      itemBarcode: document.getElementById("circBarcode").value,
      memberId: document.getElementById("circMember").value
    })
  })).body);
};

document.getElementById("checkinBtn").onclick = async () => {
  show("circOut", (await api("/api/v1/circulation/checkin", {
    method: "POST", headers: headers(),
    body: JSON.stringify({ barcode: document.getElementById("circBarcode").value })
  })).body);
};

document.getElementById("tagBtn").onclick = async () => {
  show("rfidOut", (await api("/api/v1/rfid/tag", {
    method: "POST", headers: headers(),
    body: JSON.stringify({
      barcode: document.getElementById("tagBarcode").value,
      tagId: document.getElementById("tagId").value
    })
  })).body);
};

document.getElementById("readerBtn").onclick = async () => {
  show("rfidOut", (await api("/api/v1/rfid/scan", { headers: headers(false) })).body);
};

document.getElementById("invBtn").onclick = async () => {
  const tags = encodeURIComponent(document.getElementById("invTags").value);
  show("invOut", (await api("/api/v1/inventory/scan?tags=" + tags, { headers: headers(false) })).body);
};

document.getElementById("gateBtn").onclick = async () => {
  show("gateOut", (await api("/api/v1/rfid/gate-event", {
    method: "POST", headers: headers(),
    body: JSON.stringify({
      gateId: document.getElementById("gateId").value,
      tagId: document.getElementById("gateTag").value
    })
  })).body);
};

document.getElementById("gateListBtn").onclick = async () => {
  show("gateOut", (await api("/api/v1/rfid/gate-events", { headers: headers(false) })).body);
};

document.getElementById("migBtn").onclick = async () => {
  const file = document.getElementById("migFile").files[0];
  if (!file) { show("migOut", "Choose a CSV file first"); return; }
  const form = new FormData();
  form.append("file", file);
  const dry = document.getElementById("dryRun").checked;
  const res = await fetch("/api/v1/migration/upload?dryRun=" + dry, {
    method: "POST",
    headers: state.token ? { Authorization: "Bearer " + state.token } : {},
    body: form
  });
  show("migOut", await res.json());
};

document.getElementById("statsBtn").onclick = async () => {
  show("dashOut", (await api("/api/v1/dashboard/stats", { headers: headers(false) })).body);
};

document.getElementById("reportBtn").onclick = async () => {
  show("dashOut", (await api("/api/v1/reports/circulation", { headers: headers(false) })).body);
};

setSession();
