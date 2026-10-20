// US04 ค้นหาลาน

const searchInput = document.getElementById('search-input');
let searchTimer = null;

searchInput.addEventListener('input', () => {
    clearTimeout(searchTimer);
    // รอหยุดพิมพ์ 300 ms ค่อยเรียก API
    searchTimer = setTimeout(() => {
        appState.keyword = searchInput.value.trim();
        loadLots();
    }, 300);
});

document.getElementById('search-clear').addEventListener('click', () => {
    searchInput.value = '';
    appState.keyword = '';
    loadLots();
    map.setView(MAP_CENTER, MAP_ZOOM);
});
