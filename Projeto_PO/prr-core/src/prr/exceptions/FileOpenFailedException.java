package prr.exceptions;


public class FileOpenFailedException extends Exception {
	private static final long serialVersionUID = 202208091753L;

	private static final String ERROR_MESSAGE = "Erro a abrir o ficheiro: ";
		/**
		*          * @param filename name of the import file
		*                   */
	public FileOpenFailedException(String filename) {
		super(ERROR_MESSAGE + filename);
	}

		/**
		*          * @param filename name of the import file
		*                   * @param cause exception that triggered this one
		*                            */
	public FileOpenFailedException(String filename, Exception cause) {
		super(ERROR_MESSAGE + filename, cause);
	}
}
