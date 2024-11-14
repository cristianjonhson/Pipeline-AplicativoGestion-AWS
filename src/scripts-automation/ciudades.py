import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


# Asegúrate de tener el path correcto para ChromeDriver
service = Service("chromedriver.exe")

# Inicializar el servicio y el navegador
driver = webdriver.Chrome(service=service)

# Navegar a la página
driver.get("http://localhost:8082/ciudades/lista")


#--------Primera Ciudad------------

# Esperar que el botón "newCity" esté presente y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "newCity"))
)
ciudad = driver.find_element(By.ID, "newCity")
ciudad.click()

# Esperar que el campo de nombre esté presente, limpiar y enviar texto
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "nombre"))
)
input_element = driver.find_element(By.ID, "nombre")
input_element.clear()
input_element.send_keys("CiudadAutomatizada")

# Esperar el botón de guardar y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "saveCity"))
)
guardar = driver.find_element(By.ID, "saveCity")
guardar.click()

time.sleep(1)

confirm_button = WebDriverWait(driver, 10).until(
EC.element_to_be_clickable((By.CSS_SELECTOR, ".swal2-confirm"))
)
confirm_button.click()

time.sleep(1)

ok_button = WebDriverWait(driver, 10).until(
EC.visibility_of_element_located((By.CSS_SELECTOR, ".swal2-confirm"))
)
ok_button.click()

time.sleep(2)

#--------Segunda Ciudad------------

# Esperar que el botón "newCity" esté presente y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "newCity"))
)
ciudad2 = driver.find_element(By.ID, "newCity")
ciudad2.click()

# Esperar que el campo de nombre esté presente, limpiar y enviar texto
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "nombre"))
)
input_element = driver.find_element(By.ID, "nombre")
input_element.clear()
input_element.send_keys("CiudadAutomatizada2")

# Esperar el botón de guardar y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "saveCity"))
)
guardar2 = driver.find_element(By.ID, "saveCity")
guardar2.click()

time.sleep(1)

confirm_button2 = WebDriverWait(driver, 10).until(
EC.element_to_be_clickable((By.CSS_SELECTOR, ".swal2-confirm"))
)
confirm_button2.click()

time.sleep(1)

ok_button2 = WebDriverWait(driver, 10).until(
EC.visibility_of_element_located((By.CSS_SELECTOR, ".swal2-confirm"))
)
ok_button2.click()

time.sleep(2)

#--------------Editar ciudad--------------

# Espera hasta que la tabla esté presente
WebDriverWait(driver, 10).until(
EC.presence_of_element_located((By.XPATH, "//tbody/tr"))
)
    
# Encuentra todos los botones "Editar" en la tabla
editar_botones = driver.find_elements(By.XPATH, "//tbody/tr/td/a[contains(@class, 'btn-warning')]")

# Toma el último botón "Editar"
if editar_botones:
    ultimo_boton_editar = editar_botones[-1]
    ultimo_boton_editar.click()  # Haz clic en el botón "Editar"
else:
    print("No se encontraron botones de editar.")

# Esperar que el campo de nombre esté presente, limpiar y enviar texto
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "nombre"))
)
input_element = driver.find_element(By.ID, "nombre")
input_element.clear()
input_element.send_keys("CiudadEditada")

# Esperar el botón de guardar y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "saveCity"))
)
guardar2 = driver.find_element(By.ID, "saveCity")
guardar2.click()

time.sleep(1)

confirm_button2 = WebDriverWait(driver, 10).until(
EC.element_to_be_clickable((By.CSS_SELECTOR, ".swal2-confirm"))
)
confirm_button2.click()

time.sleep(1)

ok_button2 = WebDriverWait(driver, 10).until(
EC.visibility_of_element_located((By.CSS_SELECTOR, ".swal2-confirm"))
)
ok_button2.click()

time.sleep(3)

#-------------Eliminar Ciudad------------

botones_eliminar = driver.find_elements(By.CLASS_NAME, "btn-danger")

# Verifica si hay suficientes botones
if len(botones_eliminar) >= 2:
    # Obtener el penúltimo botón (índice -2 para el penúltimo)
    penultimo_boton_eliminar = botones_eliminar[-2]
    penultimo_boton_eliminar.click()  # Simula un clic en el penúltimo botón "Eliminar"
else:
    print("No hay suficientes botones 'Eliminar'.")

time.sleep(1)

confirm_button2 = WebDriverWait(driver, 10).until(
EC.element_to_be_clickable((By.CSS_SELECTOR, ".swal2-confirm"))
)
confirm_button2.click()

time.sleep(1)

ok_button2 = WebDriverWait(driver, 10).until(
EC.visibility_of_element_located((By.CSS_SELECTOR, ".swal2-confirm"))
)
ok_button2.click()

time.sleep(3)


# Cerrar el navegador
driver.quit()
