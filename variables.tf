# Variable para el CIDR block de la VPC
variable "vpc_cidr" {
  description = "El bloque CIDR de la VPC"  # Descripción de la variable
  default     = "10.0.0.0/16"               # Valor por defecto para el CIDR block de la VPC
}

# Variable para el CIDR block de la subred pública
variable "subnet_cidr" {
  description = "El bloque CIDR de la subred publica"  # Descripción de la variable
  default     = "10.0.1.0/24"                          # Valor por defecto para el CIDR block de la subred
}

# Variable para la zona de disponibilidad de la subred
variable "availability_zone" {
  description = "La zona de disponibilidad donde se creará la subred"  # Descripción de la variable
  default     = "us-east-1a"                                           # Valor por defecto para la zona de disponibilidad
}

# Variable para el ID de la AMI (Amazon Machine Image) de la instancia EC2
variable "ami_id" {
  description = "El ID de la AMI para la instancia EC2"       # Descripción de la variable
  default     = "ami-0c0b74d29acd0cd97"                       # Valor por defecto para el ID de la AMI
}

# Variable para el tipo de instancia EC2
variable "instance_type" {
  description = "El tipo de instancia EC2"          # Descripción de la variable
  default     = "t2.micro"                          # Valor por defecto para el tipo de instancia
}

# Variable para la dirección IP permitida para acceder por SSH a la instancia EC2
variable "ssh_ip" {
  description = "La dirección IP específica que permitirá el acceso SSH"  # Descripción de la variable
  default     = "192.168.0.11/32"                                         # Valor por defecto para la dirección IP permitida
}

# Variable para el rango de direcciones IP del servicio Conexión de instancias de EC2
variable "ec2_connect_ips" {
  description = "Las direcciones IP del servicio Conexión de instancias de EC2"  # Descripción de la variable
  default     = "18.206.107.24/29"                                               # Valor por defecto para el rango de direcciones IP
}

# Variable para el CIDR block que permite todo el tráfico
variable "all_traffic_cidr" {
  description = "El bloque CIDR que permite todo el tráfico"  # Descripción de la variable
  default     = "0.0.0.0/0"                                   # Valor por defecto para permitir todo el tráfico
}
