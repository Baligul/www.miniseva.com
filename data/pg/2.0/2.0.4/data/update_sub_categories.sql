----- Subcategories -----
----- Update the prices for MiniSevaLMS.com in dollar
ALTER TABLE sub_categories RENAME price TO price_inr;
ALTER TABLE sub_categories ADD COLUMN price_usd INTEGER;
UPDATE sub_categories SET price_usd = 59;