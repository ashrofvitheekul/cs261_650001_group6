const appState = { keyword: '', markers: {}, zones: {}, selected: null };

const map = L.map('map').setView(MAP_CENTER, MAP_ZOOM);
L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors',
}).addTo(map);
const zoneLayer = L.layerGroup().addTo(map);
const markerLayer = L.layerGroup().addTo(map);

// เขตลาน
const ZONE_STYLE = { color: '#7b1113', weight: 2, fillColor: '#7b1113', fillOpacity: 0.25 };
const ZONE_SELECTED = { weight: 3, fillOpacity: 0.55 };

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text ?? '';
    return div.innerHTML;
}

function showMessage(text, isError = false) {
    const box = document.getElementById('message');
    box.textContent = text;
    box.classList.toggle('error', isError);
    box.hidden = !text;
}

function lotIcon(code, selected) {
    return L.divIcon({
        className: '',
        html: `<span class="lot-pin${selected ? ' selected' : ''}">${escapeHtml(code)}</span>`,
        iconSize: [36, 36],
        iconAnchor: [18, 18],
        popupAnchor: [0, -18],
    });
}

function renderLots(lots) {
    markerLayer.clearLayers();
    zoneLayer.clearLayers();
    appState.markers = {};
    appState.zones = {};
    appState.selected = null;
    const list = document.getElementById('lot-list');
    list.innerHTML = '';

    const count = lots.length === 0 ? ''
        : appState.keyword ? `ผลการค้นหา ${lots.length} ลาน` : `ลานจอดทั้งหมด ${lots.length} ลาน`;
    document.getElementById('lot-count').textContent = count;
    document.getElementById('search-hint').hidden = !appState.keyword;

    lots.forEach((lot) => {
        const title = `${escapeHtml(lot.code)} · ${escapeHtml(lot.nameTh)}`;
        const marker = L.marker([lot.latitude, lot.longitude], { icon: lotIcon(lot.code, false), title: lot.code })
            .bindPopup(`<strong>${title}</strong><br><small>${escapeHtml(lot.building)}</small>`);
        marker.on('click', () => selectLot(lot.code));
        marker.addTo(markerLayer);
        appState.markers[lot.code] = marker;

        if (lot.zone) {
            const zone = L.polygon(JSON.parse(lot.zone), ZONE_STYLE).addTo(zoneLayer);
            zone.on('click', () => focusLot(lot.code));
            appState.zones[lot.code] = zone;
        }

        const item = document.createElement('li');
        item.className = 'lot';
        item.innerHTML = `
            <button type="button" class="lot-name">
                <strong>${escapeHtml(lot.code)}</strong>
                <span>${escapeHtml(lot.nameTh)}</span>
            </button>
            <a class="nav-link" href="${directionsUrl(lot.latitude, lot.longitude)}" target="_blank" rel="noopener">นำทาง</a>`;
        item.querySelector('.lot-name').addEventListener('click', () => focusLot(lot.code));
        list.appendChild(item);
    });
}

function selectLot(code) {
    const previous = appState.markers[appState.selected];
    if (previous) previous.setIcon(lotIcon(appState.selected, false));
    appState.zones[appState.selected]?.setStyle(ZONE_STYLE);
    const marker = appState.markers[code];
    if (!marker) return;
    marker.setIcon(lotIcon(code, true));
    appState.zones[code]?.setStyle(ZONE_SELECTED);
    appState.selected = code;
}

function focusLot(code) {
    const marker = appState.markers[code];
    if (!marker) return;
    selectLot(code);
    map.setView(marker.getLatLng(), 18);
    marker.openPopup();
}

async function loadLots() {
    try {
        const lots = await fetchLots(appState.keyword);
        renderLots(lots);
        if (lots.length === 0) {
            showMessage(appState.keyword ? `ไม่พบลานจอดที่ตรงกับ ${appState.keyword}` : 'ยังไม่มีข้อมูลลานจอด');
        } else {
            showMessage('');
        }
    } catch (err) {
        renderLots([]);
        showMessage('โหลดข้อมูลลานจอดไม่สำเร็จ กรุณาลองใหม่', true);
    }
}

document.querySelectorAll('.menu a').forEach((link) => {
    link.addEventListener('click', (event) => {
        event.preventDefault();
        document.querySelectorAll('.menu a').forEach((a) => a.classList.toggle('active', a === link));
        document.getElementById('page-title').textContent = link.textContent;
        const target = document.getElementById(link.dataset.target);
        target.scrollIntoView({ behavior: 'smooth', block: 'center' });
        target.focus();
    });
});

loadLots();
