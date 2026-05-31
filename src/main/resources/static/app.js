const grid = document.querySelector("#productGrid");
const form = document.querySelector("#filterForm");
const categoryButtons = document.querySelectorAll(".category-button");
const menuButton = document.querySelector("#menuButton");
const openMenuButton = document.querySelector("#openMenuButton");
const menuPanel = document.querySelector("#menuPanel");
const menuOverlay = document.querySelector("#menuOverlay");
const closeMenu = document.querySelector("#closeMenu");
const menuCartShortcut = document.querySelector("#menuCartShortcut");
const cartButton = document.querySelector("#cartButton");
const cartPanel = document.querySelector("#cartPanel");
const closeCart = document.querySelector("#closeCart");
const cartCount = document.querySelector("#cartCount");
const cartSummary = document.querySelector("#cartSummary");
const cartItems = document.querySelector("#cartItems");
const cartEmpty = document.querySelector("#cartEmpty");
const cartTotal = document.querySelector("#cartTotal");
const checkoutButton = document.querySelector("#checkoutButton");
const imageModal = document.querySelector("#imageModal");
const modalImage = document.querySelector("#modalImage");
const modalCaption = document.querySelector("#modalCaption");
const closeImageModal = document.querySelector("#closeImageModal");
const productCount = document.querySelector("#productCount");
const activeFilterSummary = document.querySelector("#activeFilterSummary");

let products = [];
let selectedCategory = "all";
let cart = [];

function authHeader() {
    return "Basic " + btoa("user:1234");
}

function formatPrice(value) {
    return new Intl.NumberFormat("tr-TR", { style: "currency", currency: "TRY" }).format(value);
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

function scrollToTarget(selector) {
    const target = document.querySelector(selector);
    if (!target) {
        return;
    }
    target.scrollIntoView({ behavior: "smooth", block: "start" });
}

function productCard(product) {
    const card = document.createElement("article");
    card.className = "product-card";

    const imageWrap = document.createElement("button");
    imageWrap.type = "button";
    imageWrap.className = "product-image";
    imageWrap.setAttribute("aria-label", `${product.name} görselini aç`);
    imageWrap.addEventListener("click", () => openImageModal(product));

    const img = document.createElement("img");
    img.src = product.imageUrl;
    img.alt = product.name;
    imageWrap.append(img);

    const info = document.createElement("div");
    info.className = "product-info";

    const title = document.createElement("h3");
    title.textContent = product.name;

    const meta = document.createElement("p");
    meta.textContent = `${product.category} · ${product.material}`;

    const bottom = document.createElement("div");
    bottom.className = "product-bottom";

    const price = document.createElement("span");
    price.textContent = formatPrice(product.price);

    const button = document.createElement("button");
    button.type = "button";
    button.textContent = "Sepete ekle";
    button.addEventListener("click", () => addToCart(product));

    bottom.append(price, button);
    info.append(title, meta, bottom);
    card.append(imageWrap, info);
    return card;
}

function renderProducts() {
    grid.textContent = "";
    const visibleProducts = selectedCategory === "all"
        ? products
        : products.filter(product => product.category === selectedCategory);

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

function openImageModal(product) {
    modalImage.src = product.imageUrl;
    modalImage.alt = product.name;
    modalCaption.textContent = product.name;
    imageModal.classList.add("open");
    imageModal.setAttribute("aria-hidden", "false");
}

function closeModal() {
    imageModal.classList.remove("open");
    imageModal.setAttribute("aria-hidden", "true");
    modalImage.src = "";
    modalCaption.textContent = "";
}

async function loadProducts(event) {
    if (event) {
        event.preventDefault();
    }

    const params = new URLSearchParams();
    params.set("minPrice", document.querySelector("#minPrice").value || "0");
    params.set("maxPrice", document.querySelector("#maxPrice").value || "999999");

    const search = document.querySelector("#model").value.trim();

    if (search) {
        params.set("model", search);
    }

    grid.textContent = "Ürünler yükleniyor...";
    activeFilterSummary.textContent = buildFilterSummary();

    const response = await fetch(`/api/products/filter?${params}`, { headers: { Authorization: authHeader() } });
    if (!response.ok) {
        grid.textContent = "Ürünler yüklenemedi. Lütfen daha sonra tekrar deneyin.";
        return;
    }

    products = await response.json();
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
menuCartShortcut.addEventListener("click", () => {
    closeMenuPanel();
    openCartPanel();
});

cartButton.addEventListener("click", openCartPanel);
closeCart.addEventListener("click", closeCartPanel);
closeImageModal.addEventListener("click", closeModal);
imageModal.addEventListener("click", event => {
    if (event.target === imageModal) {
        closeModal();
    }
});
document.addEventListener("keydown", event => {
    if (event.key === "Escape") {
        if (imageModal.classList.contains("open")) {
            closeModal();
        }
        if (menuPanel.classList.contains("open")) {
            closeMenuPanel();
        }
    }
});

document.querySelectorAll("[data-scroll-target]").forEach(button => {
    button.addEventListener("click", () => {
        scrollToTarget(button.dataset.scrollTarget);
        closeMenuPanel();
    });
});

form.addEventListener("submit", loadProducts);
loadProducts();
