package autoresindependientes;

public class Libro {

	private int id;
	private String titulo;
	private String descripcion;
	private double precio;
	private String genero;
	private int cantidadPaginas;
	private Autor autor;

	public Libro(int id, String titulo, String descripcion, double precio, String genero, int cantidadPaginas,
			Autor autor) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.precio = precio;
		this.genero = genero;
		this.cantidadPaginas = cantidadPaginas;
		this.autor = autor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		if (precio >= 0) {
			this.precio = precio;
		} else {
			System.out.println("El precio debe ser positivo.");
		}
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getCantidadPaginas() {
		return cantidadPaginas;
	}

	public void setCantidadPaginas(int cantidadPaginas) {
		if (cantidadPaginas > 0) {
			this.cantidadPaginas = cantidadPaginas;
		} else {
			System.out.println("La cantidad de páginas debe ser positiva.");
		}
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	@Override
	public String toString() {
		return "Libro [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", precio=" + precio
				+ ", genero=" + genero + ", cantidadPaginas=" + cantidadPaginas + ", autor=" + autor + "]";
	}
}
