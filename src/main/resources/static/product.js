const productDetail = document.querySelector("#productDetail");
const productImage = document.querySelector("#productImage");
const productCategory = document.querySelector("#productCategory");
const productName = document.querySelector("#productName");
const productMeta = document.querySelector("#productMeta");
const productPrice = document.querySelector("#productPrice");
const productStock = document.querySelector("#productStock");
const productDescription = document.querySelector("#productDescription");
const productBrand = document.querySelector("#productBrand");
const productSupplier = document.querySelector("#productSupplier");
const productModel = document.querySelector("#productModel");
const productMaterial = document.querySelector("#productMaterial");
const productSize = document.querySelector("#productSize");
const sizeSelector = document.querySelector("#sizeSelector");
const selectedSizeText = document.querySelector("#selectedSizeText");
const addToCartButton = document.querySelector("#addToCartButton");
const cartButton = document.querySelector("#cartButton");
const cartPanel = document.querySelector("#cartPanel");
const closeCart = document.querySelector("#closeCart");
const cartCount = document.querySelector("#cartCount");
const cartSummary = document.querySelector("#cartSummary");
const cartItems = document.querySelector("#cartItems");
const cartEmpty = document.querySelector("#cartEmpty");
const cartTotal = document.querySelector("#cartTotal");
const checkoutButton = document.querySelector("#checkoutButton");

const CART_STORAGE_KEY = "nida-butik-cart";
const authHeaderValue = "Basic " + btoa("user:1234");

let cart = loadCart();
let currentProduct = null;
let selectedSize = null;

function formatPrice(value) {
    return new Intl.NumberFormat("tr-TR", { style: "currency", currency: "TRY" }).format(value);
}

function loadCart() {
    try {
        const raw = window.localStorage.getItem(CART_STORAGE_KEY);
        if (!raw) {
            return [];
        }
        const parsed = JSON.parse(raw);
        if (!Array.isArray(parsed)) {
            return [];
        }
        return parsed.map(item => ({
            ...item,
            selectedSize: item.selectedSize || item.product?.size || "Tek beden",
        }));
    } catch {
        return [];
    }
}

function saveCart() {
    window.localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cart));
}

function resolveCartItemSize(cartItem) {
    return cartItem.selectedSize || cartItem.product.size || "Tek beden";
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
    meta.textContent = `${cartItem.product.category} · ${formatPrice(cartItem.product.price)} · Beden: ${resolveCartItemSize(cartItem)}`;

    const controls = document.createElement("div");
    controls.className = "cart-item-controls";

    const decrease = document.createElement("button");
    decrease.type = "button";
    decrease.textContent = "-";
    decrease.addEventListener("click", () => decreaseQuantity(cartItem.product.id, resolveCartItemSize(cartItem)));

    const quantity = document.createElement("span");
    quantity.textContent = cartItem.quantity;

    const increase = document.createElement("button");
    increase.type = "button";
    increase.textContent = "+";
    increase.addEventListener("click", () => addToCart(cartItem.product, resolveCartItemSize(cartItem)));

    const remove = document.createElement("button");
    remove.type = "button";
    remove.className = "remove-item";
    remove.textContent = "Sil";
    remove.addEventListener("click", () => removeFromCart(cartItem.product.id, resolveCartItemSize(cartItem)));

    controls.append(decrease, quantity, increase, remove);
    info.append(title, meta, controls);
    item.append(img, info);
    return item;
}

function renderCart() {
    cartItems.textContent = "";
    const itemCount = cart.reduce((total, item) => total + item.quantity, 0);
    const total = cart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);
    const isEmpty = itemCount === 0;

    cartCount.textContent = itemCount;
    cartSummary.textContent = `${itemCount} ürün`;
    cartTotal.textContent = formatPrice(total);
    cartEmpty.hidden = !isEmpty;
    checkoutButton.disabled = isEmpty;
    saveCart();

    cart.forEach(item => cartItems.append(renderCartItem(item)));
}

function openCartPanel() {
    cartPanel.classList.add("open");
}

function closeCartPanel() {
    cartPanel.classList.remove("open");
}

function getSizeOptions(product) {
    const rawSize = String(product?.size ?? "").trim();

    if (!rawSize || /^tek beden$/i.test(rawSize)) {
        return [rawSize || "Tek beden"];
    }

    const splitSizes = rawSize
        .split(/[,/|]+/)
        .map(size => size.trim())
        .filter(Boolean);

    if (splitSizes.length > 1) {
        return [...new Set(splitSizes)];
    }

    const normalized = rawSize.toUpperCase();
    const letterSizes = ["XS", "S", "M", "L", "XL", "XXL"];
    if (letterSizes.includes(normalized)) {
        return letterSizes;
    }

    const numericSize = Number.parseInt(rawSize, 10);
    if (!Number.isNaN(numericSize)) {
        const sizes = [numericSize - 4, numericSize - 2, numericSize, numericSize + 2];
        return [...new Set(sizes.filter(size => size > 0).map(String))];
    }

    return [rawSize];
}

function getPreferredSize(product, explicitSize = null) {
    if (explicitSize) {
        return explicitSize;
    }

    const options = getSizeOptions(product);
    return options[0] || product.size || "Tek beden";
}

function renderSizeSelector(product) {
    if (!sizeSelector) {
        return;
    }

    const options = getSizeOptions(product);
    selectedSize = options[0] || product.size || "Tek beden";
    selectedSizeText.textContent = `Seçili beden: ${selectedSize}`;
    productSize.textContent = options.join(" / ");
    sizeSelector.textContent = "";

    options.forEach(size => {
        const button = document.createElement("button");
        button.type = "button";
        button.className = "size-option";
        button.textContent = size;
        button.setAttribute("aria-pressed", String(size === selectedSize));
        button.addEventListener("click", () => {
            selectedSize = size;
            selectedSizeText.textContent = `Seçili beden: ${selectedSize}`;
            sizeSelector.querySelectorAll(".size-option").forEach(option => {
                const isActive = option.textContent === selectedSize;
                option.classList.toggle("is-selected", isActive);
                option.setAttribute("aria-pressed", String(isActive));
            });
        });

        if (size === selectedSize) {
            button.classList.add("is-selected");
        }

        sizeSelector.append(button);
    });
}

function addToCart(product, explicitSize = null, openPanel = true) {
    const resolvedSize = getPreferredSize(product, explicitSize);
    const existing = cart.find(item => item.product.id === product.id && resolveCartItemSize(item) === resolvedSize);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ product, quantity: 1, selectedSize: resolvedSize });
    }
    renderCart();
    if (openPanel) {
        openCartPanel();
    }
}

function decreaseQuantity(productId, selectedSizeValue) {
    const existing = cart.find(item => item.product.id === productId && resolveCartItemSize(item) === selectedSizeValue);
    if (!existing) {
        return;
    }
    existing.quantity -= 1;
    if (existing.quantity <= 0) {
        removeFromCart(productId, selectedSizeValue);
        return;
    }
    renderCart();
}

function removeFromCart(productId, selectedSizeValue) {
    cart = cart.filter(item => !(item.product.id === productId && resolveCartItemSize(item) === selectedSizeValue));
    renderCart();
}

function getProductId() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

async function loadProduct() {
    const id = getProductId();
    if (!id) {
        productName.textContent = "Ürün bulunamadı";
        productDescription.textContent = "Geçerli bir ürün kimliği verilmedi.";
        return;
    }

    const response = await fetch(`/api/products/${id}`, { headers: { Authorization: authHeaderValue } });
    if (!response.ok) {
        productName.textContent = "Ürün bulunamadı";
        productDescription.textContent = "Bu ürün şu anda erişilebilir değil.";
        return;
    }

    currentProduct = await response.json();
    document.title = `${currentProduct.name} | Nida Butik`;

    productImage.src = currentProduct.imageUrl;
    productImage.alt = currentProduct.name;
    productCategory.textContent = currentProduct.category;
    productName.textContent = currentProduct.name;
    productMeta.textContent = `${currentProduct.brand} · ${currentProduct.material}`;
    productPrice.textContent = formatPrice(currentProduct.price);
    productStock.textContent = `${currentProduct.stockQuantity} adet stokta`;
    productDescription.textContent = "Modern kesim, temiz silüet ve günlük kullanım için tasarlanmış güçlü bir parça.";
    productBrand.textContent = currentProduct.brand;
    productSupplier.textContent = currentProduct.supplier;
    productModel.textContent = currentProduct.model;
    productMaterial.textContent = currentProduct.material;
    renderSizeSelector(currentProduct);
}

cartButton.addEventListener("click", openCartPanel);
closeCart.addEventListener("click", closeCartPanel);
addToCartButton.addEventListener("click", () => {
    if (currentProduct) {
        addToCart(currentProduct, selectedSize);
    }
});

loadProduct();
renderCart();
