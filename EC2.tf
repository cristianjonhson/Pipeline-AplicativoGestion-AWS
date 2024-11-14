provider "aws" {
  region = "us-east-1"
}

# Crear una VPC
resource "aws_vpc" "free_tier_vpc" {
  cidr_block = var.vpc_cidr

  tags = {
    Name = "free-tier-vpc"
  }
}

# Crear una subred pública
resource "aws_subnet" "free_tier_subnet" {
  vpc_id            = aws_vpc.free_tier_vpc.id
  cidr_block        = var.subnet_cidr
  availability_zone = var.availability_zone

  tags = {
    Name = "free-tier-subnet"
  }
}

# Crear una puerta de enlace de internet
resource "aws_internet_gateway" "free_tier_igw" {
  vpc_id = aws_vpc.free_tier_vpc.id

  tags = {
    Name = "free-tier-igw"
  }
}

# Crear una tabla de rutas
resource "aws_route_table" "free_tier_route_table" {
  vpc_id = aws_vpc.free_tier_vpc.id

  route {
    cidr_block = var.all_traffic_cidr
    gateway_id = aws_internet_gateway.free_tier_igw.id
  }

  tags = {
    Name = "free-tier-route-table"
  }
}

# Asociar la tabla de rutas a la subred
resource "aws_route_table_association" "free_tier_route_table_association" {
  subnet_id      = aws_subnet.free_tier_subnet.id
  route_table_id = aws_route_table.free_tier_route_table.id
}

# Crear un grupo de seguridad
resource "aws_security_group" "free_tier_sg" {
  vpc_id      = aws_vpc.free_tier_vpc.id
  description = "Example security group for SSH and HTTP access"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.ssh_ip]  # Permitir tráfico SSH desde una dirección IP específica
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.ec2_connect_ips]  # Direcciones IP del servicio Conexión de instancias de EC2
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = [var.all_traffic_cidr]  # Permitir tráfico HTTP desde cualquier dirección IP
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = [var.all_traffic_cidr]  # Permitir tráfico HTTPS desde cualquier dirección IP
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = [var.all_traffic_cidr]  # Permitir todo el tráfico de salida
  }

  tags = {
    Name = "free-tier-sg"
  }
}

# Crear una instancia EC2
resource "aws_instance" "free_tier_instance" {
  ami                         = var.ami_id
  instance_type               = var.instance_type
  subnet_id                   = aws_subnet.free_tier_subnet.id
  associate_public_ip_address = true  # Asignar una dirección IPv4 pública a la instancia
  vpc_security_group_ids      = [aws_security_group.free_tier_sg.id]

  tags = {
    Name = "free-tier-instance"
  }
}
