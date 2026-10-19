// US05: find the closest lot with free spots and open directions to it

const nearestBox = document.getElementById('nearest-result');

let userMarker = null;
let routeLine = null;

function selectedVehicleType() {
    return document.querySelector('input[name="vehicle"]:checked').value;
}

function roundedMeters(meters) {
    return Math.round(meters / 10) * 10;
}

function showNearest(html, isError = false) {
    nearestBox.innerHTML = html;
    nearestBox.classList.toggle('nearest-error', isError); 
    nearestBox.hidden = false;
}

function clearRoute() {
    if (routeLine) { map.removeLayer(routeLine); routeLine = null; }
    if (userMarker) { map.removeLayer(userMarker); userMarker = null; }
}

function navigateButton(lat, lng, origin) {
    let url = directionsUrl(lat, lng);
    return `<a class="btn-navigate" href="${url}" target="_blank" rel="noopener">นำทาง (เปิด Google Maps)</a>`;
}

// ใช้ตำแหน่งไม่ได้ / ผิดพลาด: กล่องแดง + ทุกลานมีปุ่มนำทาง
function showAllLotsFallback(message) {
    const items = appState.lots.map((lot) => `
        <div class="nearest-card">
            <strong>${escapeHtml(lot.code)} · ${escapeHtml(lot.nameTh)}</strong><br>
            ${navigateButton(lot.latitude, lot.longitude)}
        </div>`).join('');
    showNearest(`<div class="nearest-msg">${escapeHtml(message)}</div>${items}`, true);
}

document.getElementById('nearest-btn').addEventListener('click', () => {
    clearRoute();

    if (!navigator.geolocation) {
        showAllLotsFallback('เบราว์เซอร์นี้ใช้ตำแหน่งไม่ได้ เลือกลานด้านล่างเพื่อนำทาง');
        return;
    }
    showNearest('กำลังหาตำแหน่งของคุณ...');

    navigator.geolocation.getCurrentPosition(async (position) => {
        const { latitude, longitude } = position.coords;
        try {
            const result = await fetchNearest(latitude, longitude, selectedVehicleType());
            const lot = result.lot;

            showNearest(`
                <div class="nearest-card">
                    <strong>${escapeHtml(lot.code)} · ${escapeHtml(lot.nameTh)}</strong><br>
                    ห่างประมาณ ${roundedMeters(result.distanceMeters)} เมตร<br>
                    ${navigateButton(lot.latitude, lot.longitude)}
                </div>`);

            userMarker = L.circleMarker([latitude, longitude], {
                radius: 8, color: '#fff', weight: 2, fillColor: '#2563eb', fillOpacity: 1
            }).addTo(map);
            routeLine = L.polyline([[latitude, longitude], [lot.latitude, lot.longitude]], {
                color: '#d32f2f', weight: 4, dashArray: '8 8'
            }).addTo(map);
            map.fitBounds(routeLine.getBounds(), { padding: [60, 60] });

            focusLot(lot.code);
        } catch (err) {
            if (err.status === 404) {
               
                showAllLotsFallback(err.message);
            } else {
                showAllLotsFallback('หาลานใกล้ที่สุดไม่สำเร็จ กรุณาลองใหม่');
            }
        }
    }, () => {
        showAllLotsFallback('ไม่สามารถใช้ตำแหน่งของคุณได้ กรุณาอนุญาตการเข้าถึงตำแหน่ง หรือเลือกลานด้านล่าง');
    }, { timeout: 10000 });
});