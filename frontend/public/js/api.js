async function requestJson(path) {
    const res = await fetch(`${API_BASE}${path}`);
    const body = await res.json().catch(() => ({}));
    if (!res.ok) {
        const error = new Error(body.message || `HTTP ${res.status}`);
        error.status = res.status;
        throw error;
    }
    return body;
}

function fetchLots(keyword) {
    const query = keyword ? `?q=${encodeURIComponent(keyword)}` : '';
    return requestJson(`/api/parking-lots${query}`);
}

function fetchNearest(lat, lng, type) {
    return requestJson(`/api/parking-lots/nearest?lat=${lat}&lng=${lng}&type=${type}`);
}

function directionsUrl(lat, lng) {
    return `https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`;
}
