// US05: find the closest lot of the chosen vehicle type and open directions to it

const nearestBox = document.getElementById('nearest-result');
const routeLayer = L.layerGroup().addTo(map);

function selectedVehicleType() {
    return document.querySelector('input[name="vehicle"]:checked').value;
}

// isAlert = red box (no location / error)
function showNearest(html, isAlert = false) {
    nearestBox.innerHTML = html;
    nearestBox.classList.toggle('alert', isAlert);
    nearestBox.hidden = false;
}

function roundedMeters(meters) {
    return Math.round(meters / 10) * 10;
}

// Location blocked: every lot in the list shows its own "นำทาง" button (Figma screen 11)
function showNavInList(show) {
    document.body.classList.toggle('show-nav', show);
}

function locationBlocked() {
    showNearest('ไม่สามารถใช้ตำแหน่งของคุณได้ เลือกลานจากรายการเพื่อนำทาง', true);
    showNavInList(true);
}

document.getElementById('nearest-btn').addEventListener('click', () => {
    routeLayer.clearLayers();
    if (!navigator.geolocation) {
        locationBlocked();
        return;
    }
    showNearest('กำลังหาตำแหน่งของคุณ...');

    navigator.geolocation.getCurrentPosition(async (position) => {
        const { latitude, longitude } = position.coords;
        try {
            const result = await fetchNearest(latitude, longitude, selectedVehicleType());
            const lot = result.lot;
            showNavInList(false);
            showNearest(`
                <div class="result-card">
                    <strong>${escapeHtml(lot.code)}</strong>
                    <div>${escapeHtml(lot.nameTh)}</div>
                    <div class="distance">ห่างประมาณ ${roundedMeters(result.distanceMeters)} เมตร</div>
                </div>
                <a class="btn-primary" href="${directionsUrl(lot.latitude, lot.longitude)}" target="_blank" rel="noopener">นำทาง (เปิด Google Maps)</a>`);
            // Figma: black dot = you, dashed line = straight way to the lot
            L.circleMarker([latitude, longitude], { radius: 6, color: '#fff', weight: 2, fillColor: '#111', fillOpacity: 1 })
                .addTo(routeLayer);
            L.polyline([[latitude, longitude], [lot.latitude, lot.longitude]], { color: '#7b1113', weight: 3, dashArray: '8 8' })
                .addTo(routeLayer);
            focusLot(lot.code);
        } catch (err) {
            // 404 = no lot of this vehicle type; the API sends the Thai message
            showNearest(escapeHtml(err.status === 404 ? err.message : 'หาลานใกล้ที่สุดไม่สำเร็จ กรุณาลองใหม่'), true);
        }
    }, locationBlocked, { timeout: 10000 });
});
