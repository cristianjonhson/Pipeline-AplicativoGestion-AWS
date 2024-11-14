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
driver.get("http://localhost:8082/usuarios/lista")


#---------Primer Usuario-------------

# Esperar que el botón "newCity" esté presente y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "newUser"))
)
usuario = driver.find_element(By.ID, "newUser")
usuario.click()

# Esperar que el campo de nombre esté presente, limpiar y enviar texto
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "nombre"))
)
input_element = driver.find_element(By.ID, "nombre")
input_element.clear()
input_element.send_keys("Nombre automatizado")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "apellido"))
)
input_element = driver.find_element(By.ID, "apellido")
input_element.clear()
input_element.send_keys("Apellido automatizado")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "contrasena"))
)
input_element = driver.find_element(By.ID, "contrasena")
input_element.clear()
input_element.send_keys("123456")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "email"))
)
input_element = driver.find_element(By.ID, "email")
input_element.clear()
input_element.send_keys("correo@automatizado")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "rol"))
)
input_element = driver.find_element(By.ID, "rol")
input_element.clear()
input_element.send_keys("usuario")

# Esperar el botón de guardar y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "saveUser"))
)
guardar = driver.find_element(By.ID, "saveUser")
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
    EC.presence_of_element_located((By.ID, "newUser"))
)
usuario = driver.find_element(By.ID, "newUser")
usuario.click()

# Esperar que el campo de nombre esté presente, limpiar y enviar texto
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "nombre"))
)
input_element = driver.find_element(By.ID, "nombre")
input_element.clear()
input_element.send_keys("Nombre automatizado2")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "apellido"))
)
input_element = driver.find_element(By.ID, "apellido")
input_element.clear()
input_element.send_keys("Apellido automatizado2")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "contrasena"))
)
input_element = driver.find_element(By.ID, "contrasena")
input_element.clear()
input_element.send_keys("123456")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "email"))
)
input_element = driver.find_element(By.ID, "email")
input_element.clear()
input_element.send_keys("correo2@automatizado")

WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "rol"))
)
input_element = driver.find_element(By.ID, "rol")
input_element.clear()
input_element.send_keys("usuario")

# Esperar el botón de guardar y hacer clic
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.ID, "saveUser"))
)
guardar2 = driver.find_element(By.ID, "saveUser")
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

#--------------Editar Usuario--------------

# Esperar que la tabla de usuarios esté presente
WebDriverWait(driver, 10).until(
    EC.presence_of_element_located((By.XPATH, "//table[@class='table table-striped']"))
)

# Obtener todas las filas de la tabla de usuarios
rows = driver.find_elements(By.XPATH, "//table[@class='table table-striped']/tbody/tr")

# Verificar si hay al menos dos usuarios
if len(rows) < 2:
    print("No hay suficientes usuarios para realizar las acciones")
else:

#  # Obtener el penúltimo usuario
#     penultimo_usuario = rows[-2]

# # Hacer clic en el botón de edición del penúltimo usuario
#     edit_button = penultimo_usuario.find_element(By.XPATH, ".//a[contains(@class, 'btn-warning')]")
#     edit_button.click()

#  # Esperar que el formulario de edición esté disponible
#     WebDriverWait(driver, 10).until(
#         EC.presence_of_element_located((By.ID, "editarForm"))
#     )

#     # Editar los campos del formulario
#     nombre_input = driver.find_element(By.ID, "nombre")
#     nombre_input.clear()
#     nombre_input.send_keys("Nuevo Nombre Penúltimo Usuario")

#     apellido_input = driver.find_element(By.ID, "apellido")
#     apellido_input.clear()
#     apellido_input.send_keys("Nuevo Apellido")

#     # Guardar los cambios (botón de guardar debe tener el ID o la clase correspondiente)
#     save_button = driver.find_element(By.ID, "saveUser")
#     save_button.click()


#     time.sleep(1)

#     confirm_button2 = WebDriverWait(driver, 10).until(
#     EC.element_to_be_clickable((By.CSS_SELECTOR, ".swal2-confirm"))
#     )
#     confirm_button2.click()

#     time.sleep(1)

#     ok_button2 = WebDriverWait(driver, 10).until(
#     EC.visibility_of_element_located((By.CSS_SELECTOR, ".swal2-confirm"))
#     )
#     ok_button2.click()

#     time.sleep(3)

#-------------Eliminar Ciudad------------

# Esperar la tabla de usuarios de nuevo
    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//table[@class='table table-striped']"))
    )

    # Obtener de nuevo el último usuario
    rows = driver.find_elements(By.XPATH, "//table[@class='table table-striped']/tbody/tr")
    ultimo_usuario = rows[-1]

    # Hacer clic en el botón de eliminar del último usuario
    delete_button = ultimo_usuario.find_element(By.XPATH, ".//button[contains(@class, 'btn-danger')]")
    delete_button.click()

    time.sleep(1)

    # Confirmar la eliminación si aparece un SweetAlert o un diálogo de confirmación
    confirm_button = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, ".swal2-confirm"))
    )
    confirm_button.click()

    time.sleep(1)

    ok_button2 = WebDriverWait(driver, 10).until(
    EC.visibility_of_element_located((By.CSS_SELECTOR, ".swal2-confirm"))
    )
    ok_button2.click()

    # Esperar que la eliminación se complete
    time.sleep(2)


# Cerrar el navegador
driver.quit()
