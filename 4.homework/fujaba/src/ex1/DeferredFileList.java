package ex1;

import java.io.File;
import java.util.Iterator;

import de.upb.tools.fca.FHashSet;

public class DeferredFileList<E> extends FHashSet<E> {
	private boolean populated;
	private final Directory directory;

	public DeferredFileList(Directory directory) {
		this.directory = directory;
		
	}
	
	private void ensurePopulated() {
		if (populated) {
			return;
		}
		populated = true;
		File parent = directory.getFile();
		for (File child : parent.listFiles()) {
			AbstractFile abstractFile;
			if (child.isFile()) {
				abstractFile = new RegularFile(child);
			}
			else if (child.isDirectory()) {
				abstractFile = new Directory(child);
			}
			else {
				continue;
			}
			abstractFile.setParent(directory);
		}
	}

	@Override
	public synchronized boolean add(E obj) {
		ensurePopulated();
		return super.add(obj);
	}

	@Override
	public synchronized void clear() {
		ensurePopulated();
		super.clear();
	}

	@Override
	public synchronized boolean contains(Object o) {
		ensurePopulated();
		return super.contains(o);
	}

	@Override
	public synchronized boolean isEmpty() {
		ensurePopulated();
		return super.isEmpty();
	}

	@Override
	public synchronized Iterator<E> iterator() {
		ensurePopulated();
		return super.iterator();
	}

	@Override
	public synchronized boolean remove(Object obj) {
		ensurePopulated();
		return super.remove(obj);
	}

	@Override
	public synchronized int size() {
		ensurePopulated();
		return super.size();
	}
}
