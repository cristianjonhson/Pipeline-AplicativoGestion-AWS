import time
from datetime import datetime
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support import expected_conditions as EC

# Asegúrate de tener el path correcto para ChromeDriver
service = Service("chromedriver.exe")

# Inicializar el controlador (en este caso, para Chrome)
driver = webdriver.Chrome()

# Abre la página web donde se encuentra el formulario
driver.get("http://localhost:8082/evento/lista")

# Esperar que el botón "newEvent" esté presente y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "newEvent"))
)
evento = driver.find_element(By.ID, "newEvent")
evento.click()

# Llenar el campo "Título del Evento"
titulo = driver.find_element(By.ID, "titulo")
titulo.send_keys("Evento de Prueba")

# Llenar el campo "Descripción"
descripcion = driver.find_element(By.ID, "descripcion")
descripcion.send_keys("Descripción de prueba para el evento")

# Establecer fechas y horas
fecha_inicio = datetime(2024, 10, 15, 9, 0)  # 15 de octubre de 2024 a las 09:00
fecha_fin = datetime(2024, 10, 15, 17, 0)     # 15 de octubre de 2024 a las 17:00

# Convertir a formato requerido para datetime-local
fecha_inicio_str = fecha_inicio.strftime("%Y-%m-%dT%H:%M")
fecha_fin_str = fecha_fin.strftime("%Y-%m-%dT%H:%M")

# Llenar la "Fecha de Inicio"
fecha_inicio_input = driver.find_element(By.ID, "fechaInicio")
driver.execute_script("arguments[0].value = arguments[1];", fecha_inicio_input, fecha_inicio_str)

# Llenar la "Fecha de Fin"
fecha_fin_input = driver.find_element(By.ID, "fechaFin")
driver.execute_script("arguments[0].value = arguments[1];", fecha_fin_input, fecha_fin_str)

# Llenar la "Ubicación"
ubicacion = driver.find_element(By.ID, "ubicacion")
ubicacion.send_keys("Ubicación de prueba")

# Llenar el "Valor del Evento"
valor = driver.find_element(By.ID, "valor")
valor.send_keys("100")

# Seleccionar el "Organizador" del evento (usando el valor de las opciones)
organizador = Select(driver.find_element(By.ID, "organizadorId"))
organizador.select_by_value("1")  # Reemplaza "1" con el valor adecuado

# Seleccionar la "Categoría" del evento
categoria = Select(driver.find_element(By.ID, "categoriaId"))
categoria.select_by_value("2")  # Reemplaza "2" con el valor adecuado

# Seleccionar la "Ciudad"
ciudad = Select(driver.find_element(By.ID, "ciudadId"))
ciudad.select_by_value("3")  # Reemplaza "3" con el valor adecuado

# Llenar el campo "Imagen HTML"
imagen_html = driver.find_element(By.ID, "imagenHtml")
imagen_html.send_keys("Imagen de ejemplo")

# Esperar un poco para asegurarnos de que todo esté listo
time.sleep(1)

# Enviar el formulario (simula el clic en el botón "Crear")
submit_button = driver.find_element(By.XPATH, "//button[@type='submit']")
submit_button.click()

confirm_button = WebDriverWait(driver, 10).until(
    EC.element_to_be_clickable((By.CSS_SELECTOR, ".swal2-confirm"))
)
confirm_button.click()

time.sleep(1)

ok_button = WebDriverWait(driver, 10).until(
    EC.visibility_of_element_located((By.CSS_SELECTOR, ".swal2-confirm"))
)
ok_button.click()

time.sleep(3)

# Cerrar el navegador
driver.quit()
