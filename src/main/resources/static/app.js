const grid = document.querySelector("#productGrid");
const form = document.querySelector("#filterForm");
const categoryButtons = document.querySelectorAll(".category-button");
const menuButton = document.querySelector("#menuButton");
const openMenuButton = document.querySelector("#openMenuButton");
const menuPanel = document.querySelector("#menuPanel");
const menuOverlay = document.querySelector("#menuOverlay");
const closeMenu = document.querySelector("#closeMenu");
const cartButton = document.querySelector("#cartButton");
const cartPanel = document.querySelector("#cartPanel");
const closeCart = document.querySelector("#closeCart");
const cartCount = document.querySelector("#cartCount");
const cartSummary = document.querySelector("#cartSummary");
const cartItems = document.querySelector("#cartItems");
const cartEmpty = document.querySelector("#cartEmpty");
const cartTotal = document.querySelector("#cartTotal");
const checkoutButton = document.querySelector("#checkoutButton");
const productCount = document.querySelector("#productCount");
const activeFilterSummary = document.querySelector("#activeFilterSummary");
const heroCarousel = document.querySelector("#heroCarousel");
const heroSlides = heroCarousel ? Array.from(heroCarousel.querySelectorAll(".hero-slide")) : [];

let products = [];
let selectedCategory = "all";
let cart = loadCartFromStorage();
let heroSlideIndex = 0;
let heroTimer = null;
const HERO_ROTATION_MS = 10000;
const CART_STORAGE_KEY = "nida-butik-cart";

function authHeader() {
    return "Basic " + btoa("user:1234");
}

function formatPrice(value) {
    return new Intl.NumberFormat("tr-TR", { style: "currency", currency: "TRY" }).format(value);
}

function normalizeText(value) {
    return String(value ?? "")
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .toLowerCase();
}

function loadCartFromStorage() {
    try {
        const raw = window.localStorage.getItem("nida-butik-cart");
        if (!raw) {
            return [];
        }
        const parsed = JSON.parse(raw);
        return Array.isArray(parsed) ? parsed : [];
    } catch {
        return [];
    }
}

function saveCartToStorage() {
    window.localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cart));
}

function getCategoryLabel(category) {
    return category === "all" ? "Tümü" : category;
}

function buildFilterSummary() {
    const search = document.querySelector("#model").value.trim();
    const minPrice = document.querySelector("#minPrice").value || "0";
    const maxPrice = document.querySelector("#maxPrice").value || "999999";
    const parts = [getCategoryLabel(selectedCategory)];

    if (search) {
        parts.push(`ürün: ${search}`);
    }
    parts.push(`fiyat: ${formatPrice(Number(minPrice))} - ${formatPrice(Number(maxPrice))}`);

    return parts.join(" • ");
}

function getVisibleProducts() {
    const search = normalizeText(document.querySelector("#model").value.trim());
    const minPrice = Number(document.querySelector("#minPrice").value || "0");
    const maxPrice = Number(document.querySelector("#maxPrice").value || "999999");

    return products.filter(product => {
        const matchesCategory = selectedCategory === "all"
            || normalizeText(product.category) === normalizeText(selectedCategory);
        const matchesSearch = !search
            || normalizeText(product.name).includes(search)
            || normalizeText(product.model).includes(search)
            || normalizeText(product.brand).includes(search)
            || normalizeText(product.material).includes(search)
            || normalizeText(product.category).includes(search);
        const price = Number(product.price);
        const matchesPrice = price >= minPrice && price <= maxPrice;

        return matchesCategory && matchesSearch && matchesPrice;
    });
}

function showHeroSlide(index) {
    if (heroSlides.length === 0) {
        return;
    }

    heroSlideIndex = (index + heroSlides.length) % heroSlides.length;

    heroSlides.forEach((slide, slideIndex) => {
        slide.classList.toggle("is-active", slideIndex === heroSlideIndex);
    });
}

function advanceHeroSlide() {
    showHeroSlide(heroSlideIndex + 1);
}

function stopHeroRotation() {
    if (heroTimer !== null) {
        window.clearInterval(heroTimer);
        heroTimer = null;
    }
}

function startHeroRotation() {
    if (heroSlides.length < 2 || heroTimer !== null) {
        return;
    }

    heroTimer = window.setInterval(advanceHeroSlide, HERO_ROTATION_MS);
}

function setMenuOpen(isOpen) {
    menuPanel.classList.toggle("open", isOpen);
    menuOverlay.hidden = !isOpen;
    menuPanel.setAttribute("aria-hidden", String(!isOpen));
    menuButton.setAttribute("aria-expanded", String(isOpen));
    document.body.classList.toggle("menu-open", isOpen);
}

function openMenu() {
    setMenuOpen(true);
}

function closeMenuPanel() {
    setMenuOpen(false);
}

function closeCartPanel() {
    cartPanel.classList.remove("open");
}

function openCartPanel() {
    closeMenuPanel();
    cartPanel.classList.add("open");
}

function productCard(product) {
    const card = document.createElement("article");
    card.className = "product-card";
    card.addEventListener("click", event => {
        if (event.target.closest("button")) {
            return;
        }
        window.location.href = `/product/${product.id}`;
    });

    const imageWrap = document.createElement("a");
    imageWrap.href = `/product/${product.id}`;
    imageWrap.className = "product-image";

    const img = document.createElement("img");
    img.src = product.imageUrl;
    img.alt = product.name;
    imageWrap.append(img);

    const info = document.createElement("div");
    info.className = "product-info";

    const title = document.createElement("h3");
    title.className = "product-title";
    title.textContent = product.name;

    const meta = document.createElement("p");
    meta.textContent = `${product.category} · ${product.material}`;

    const bottom = document.createElement("div");
    bottom.className = "product-bottom";

    const price = document.createElement("span");
    price.textContent = formatPrice(product.price);

    const button = document.createElement("button");
    button.type = "button";
    button.textContent = "Detaya git";
    button.addEventListener("click", () => {
        window.location.href = `/product/${product.id}`;
    });

    const cartButton = document.createElement("button");
    cartButton.type = "button";
    cartButton.className = "secondary-action";
    cartButton.textContent = "Sepete ekle";
    cartButton.addEventListener("click", event => {
        event.preventDefault();
        event.stopPropagation();
        addToCart(product);
    });

    bottom.append(price, button, cartButton);
    info.append(title, meta, bottom);
    card.append(imageWrap, info);
    return card;
}

function renderProducts() {
    grid.textContent = "";
    const visibleProducts = getVisibleProducts();

    productCount.textContent = `${visibleProducts.length} ürün`;
    activeFilterSummary.textContent = buildFilterSummary();

    if (visibleProducts.length === 0) {
        grid.textContent = "Bu filtrelere uygun ürün bulunamadı.";
        return;
    }

    visibleProducts.forEach(product => grid.append(productCard(product)));
}

function renderCartItem(cartItem) {
    const item = document.createElement("li");
    item.className = "cart-item";

    const img = document.createElement("img");
    img.src = cartItem.product.imageUrl;
    img.alt = cartItem.product.name;

    const info = document.createElement("div");
    info.className = "cart-item-info";

    const title = document.createElement("strong");
    title.textContent = cartItem.product.name;

    const meta = document.createElement("span");
    meta.textContent = `${cartItem.product.category} · ${formatPrice(cartItem.product.price)}`;

    const controls = document.createElement("div");
    controls.className = "cart-item-controls";

    const decrease = document.createElement("button");
    decrease.type = "button";
    decrease.textContent = "-";
    decrease.setAttribute("aria-label", `${cartItem.product.name} adet azalt`);
    decrease.addEventListener("click", () => decreaseQuantity(cartItem.product.id));

    const quantity = document.createElement("span");
    quantity.textContent = cartItem.quantity;

    const increase = document.createElement("button");
    increase.type = "button";
    increase.textContent = "+";
    increase.setAttribute("aria-label", `${cartItem.product.name} adet artır`);
    increase.addEventListener("click", () => addToCart(cartItem.product, false));

    const remove = document.createElement("button");
    remove.type = "button";
    remove.className = "remove-item";
    remove.textContent = "Sil";
    remove.addEventListener("click", () => removeFromCart(cartItem.product.id));

    controls.append(decrease, quantity, increase, remove);
    info.append(title, meta, controls);
    item.append(img, info);
    return item;
}

function renderCart() {
    cartItems.textContent = "";
    const itemCount = cart.reduce((total, item) => total + item.quantity, 0);
    const total = cart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);

    cartCount.textContent = itemCount;
    cartSummary.textContent = `${itemCount} ürün`;
    cartTotal.textContent = formatPrice(total);
    cartEmpty.hidden = cart.length > 0;
    checkoutButton.disabled = cart.length === 0;
    saveCartToStorage();

    cart.forEach(item => {
        cartItems.append(renderCartItem(item));
    });
}

function addToCart(product, openPanel = true) {
    const existing = cart.find(item => item.product.id === product.id);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ product, quantity: 1 });
    }
    renderCart();
    if (openPanel) {
        openCartPanel();
    }
}

function decreaseQuantity(productId) {
    const existing = cart.find(item => item.product.id === productId);
    if (!existing) {
        return;
    }
    existing.quantity -= 1;
    if (existing.quantity <= 0) {
        removeFromCart(productId);
        return;
    }
    renderCart();
}

function removeFromCart(productId) {
    cart = cart.filter(item => item.product.id !== productId);
    renderCart();
}

async function loadProducts(event) {
    if (event) {
        event.preventDefault();
    }

    if (products.length === 0) {
        grid.textContent = "Ürünler yükleniyor...";
        const response = await fetch("/api/products", { headers: { Authorization: authHeader() } });
        if (!response.ok) {
            grid.textContent = "Ürünler yüklenemedi. Lütfen daha sonra tekrar deneyin.";
            return;
        }

        products = await response.json();
    }

    activeFilterSummary.textContent = buildFilterSummary();
    renderProducts();
    closeMenuPanel();
}

categoryButtons.forEach(button => {
    button.addEventListener("click", () => {
        categoryButtons.forEach(item => item.classList.remove("active"));
        button.classList.add("active");
        selectedCategory = button.dataset.category;
        renderProducts();
        closeMenuPanel();
    });
});

menuButton.addEventListener("click", () => {
    if (menuPanel.classList.contains("open")) {
        closeMenuPanel();
        return;
    }
    openMenu();
});

openMenuButton.addEventListener("click", openMenu);
closeMenu.addEventListener("click", closeMenuPanel);
menuOverlay.addEventListener("click", closeMenuPanel);

cartButton.addEventListener("click", openCartPanel);
closeCart.addEventListener("click", closeCartPanel);
document.addEventListener("keydown", event => {
    if (event.key === "Escape") {
        if (menuPanel.classList.contains("open")) {
            closeMenuPanel();
        }
    }
});

form.addEventListener("submit", loadProducts);
renderCart();
loadProducts();

if (heroCarousel && heroSlides.length > 1) {
    startHeroRotation();
}
