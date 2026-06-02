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
        return Array.isArray(parsed) ? parsed : [];
    } catch {
        return [];
    }
}

function saveCart() {
    window.localStorage.setItem(CART_STORAGE_KEY, JSON.stringify(cart));
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
    decrease.addEventListener("click", () => decreaseQuantity(cartItem.product.id));

    const quantity = document.createElement("span");
    quantity.textContent = cartItem.quantity;

    const increase = document.createElement("button");
    increase.type = "button";
    increase.textContent = "+";
    increase.addEventListener("click", () => addToCart(cartItem.product));

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
    saveCart();

    cart.forEach(item => cartItems.append(renderCartItem(item)));
}

function openCartPanel() {
    cartPanel.classList.add("open");
}

function closeCartPanel() {
    cartPanel.classList.remove("open");
}

function addToCart(product) {
    const existing = cart.find(item => item.product.id === product.id);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ product, quantity: 1 });
    }
    renderCart();
    openCartPanel();
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
}

cartButton.addEventListener("click", openCartPanel);
closeCart.addEventListener("click", closeCartPanel);
addToCartButton.addEventListener("click", () => {
    if (currentProduct) {
        addToCart(currentProduct);
    }
});

loadProduct();
renderCart();
