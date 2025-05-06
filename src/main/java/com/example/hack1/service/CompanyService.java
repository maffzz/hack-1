package com.example.hack1.service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final RestriccionRepository restriccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    public Restriccion crearRestriccion(Long empresaId, Restriccion restriccion) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
        restriccion.setEmpresa(empresa);
        return restriccionRepository.save(restriccion);
    }

    public List<Restriccion> listarRestricciones(Long empresaId) {
        return restriccionRepository.findByEmpresaId(empresaId);
    }

    public Restriccion actualizarRestriccion(Long restriccionId, Restriccion datos) {
        Restriccion restriccion = restriccionRepository.findById(restriccionId)
                .orElseThrow(() -> new EntityNotFoundException("Restricción no encontrada"));
        restriccion.setModelo(datos.getModelo());
        restriccion.setLimitePorTiempo(datos.getLimitePorTiempo());
        restriccion.setTokensMaximos(datos.getTokensMaximos());
        return restriccionRepository.save(restriccion);
    }

    public void eliminarRestriccion(Long restriccionId) {
        restriccionRepository.deleteById(restriccionId);
    }

    public Usuario crearUsuario(Long empresaId, Usuario usuario) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
        usuario.setEmpresa(empresa);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios(Long empresaId) {
        return usuarioRepository.findByEmpresaId(empresaId);
    }

    public Usuario obtenerUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public Usuario actualizarUsuario(Long usuarioId, Usuario datos) {
        Usuario usuario = obtenerUsuario(usuarioId);
        usuario.setNombre(datos.getNombre());
        usuario.setEmail(datos.getEmail());
        usuario.setRol(datos.getRol());
        return usuarioRepository.save(usuario);
    }

    public Usuario asignarLimites(Long usuarioId, List<Limite> limites) {
        Usuario usuario = obtenerUsuario(usuarioId);
        for (Limite limite : limites) {
            limite.setUsuario(usuario);
        }
        usuario.getLimites().addAll(limites);
        return usuarioRepository.save(usuario);
    }

}