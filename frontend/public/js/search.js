// US04: search by building, lot name or lot code (map.js does the drawing)

const searchInput = document.getElementById('search-input');
let searchTimer = null;

searchInput.addEventListener('input', () => {
    clearTimeout(searchTimer);
  
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