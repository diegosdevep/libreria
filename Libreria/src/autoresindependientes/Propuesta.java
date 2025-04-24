package autoresindependientes;

public class Propuesta {

	private int id;
	private String titulo;
	private String descripcion;
	private EstadoPropuesta estado;
	private String genero;
	private int cantidadPaginas;
	private Autor autor;

	public enum EstadoPropuesta {
		EN_REVISION, ACEPTADA, RECHAZADA
	}

	public Propuesta(int id, String titulo, String descripcion, String genero, int cantidadPaginas, Autor autor) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.genero = genero;
		this.cantidadPaginas = cantidadPaginas;
		this.autor = autor;
		this.estado = EstadoPropuesta.EN_REVISION;
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

	public EstadoPropuesta getEstado() {
		return estado;
	}

	public void setEstado(EstadoPropuesta estado) {
		this.estado = estado;
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
		return "Propuesta [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", estado=" + estado
				+ ", genero=" + genero + ", cantidadPaginas=" + cantidadPaginas + ", autor=" + autor + "]";
	}
}
